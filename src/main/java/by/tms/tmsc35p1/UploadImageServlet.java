package by.tms.tmsc35p1;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.UUID;

@WebServlet({"/upload", "/uploads/*"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,
        maxFileSize = 1024 * 1024 * 5,
        maxRequestSize = 1024 * 1024 * 10
)
public class UploadImageServlet extends HttpServlet {

    private final AccountDetailsStorage detailsStorage = new AccountDetailsStorage();
    private static final String UPLOAD_DIR = "uploads";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Обработка запросов на скачивание файлов (/uploads/*)
        String pathInfo = request.getPathInfo();
        if (pathInfo != null && pathInfo.startsWith("/")) {
            String filename = pathInfo.substring(1);
            String uploadPath = getServletContext().getRealPath("/" + UPLOAD_DIR);
            File file = new File(uploadPath, filename);

            if (file.exists()) {
                String mimeType = getServletContext().getMimeType(file.getName());
                if (mimeType == null) {
                    mimeType = "application/octet-stream";
                }

                response.setContentType(mimeType);
                response.setContentLength((int) file.length());

                try (InputStream in = new FileInputStream(file);
                     OutputStream out = response.getOutputStream()) {

                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = in.read(buffer)) != -1) {
                        out.write(buffer, 0, bytesRead);
                    }
                }
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
            return;
        }

        response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Account account = (Account) session.getAttribute("account");

        if (account == null) {
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        boolean isAjax = "XMLHttpRequest".equals(req.getHeader("X-Requested-With"));

        try {
            String uploadType = req.getParameter("type");
            Part filePart = req.getPart("file");

            if (filePart != null && filePart.getSize() > 0) {
                String originalFileName = getFileName(filePart);
                String fileExtension = getFileExtension(originalFileName);
                String uniqueFileName = UUID.randomUUID().toString() + fileExtension;

                String savedFileName = saveFile(filePart, uniqueFileName, req);

                if (savedFileName != null) {
                    Optional<AccountDetails> currentDetailsOpt = detailsStorage.getAccountDetails(account.id());
                    AccountDetails currentDetails = currentDetailsOpt.orElse(new AccountDetails(account.id()));

                    String fileUrl = "/" + UPLOAD_DIR + "/" + savedFileName;
                    AccountDetails updatedDetails;

                    if ("avatar".equals(uploadType)) {
                        updatedDetails = new AccountDetails(
                                account.id(),
                                currentDetails.email(),
                                currentDetails.bio(),
                                currentDetails.location(),
                                currentDetails.website(),
                                currentDetails.birthDate(),
                                fileUrl,
                                currentDetails.headerUrl()
                        );
                    } else {
                        updatedDetails = new AccountDetails(
                                account.id(),
                                currentDetails.email(),
                                currentDetails.bio(),
                                currentDetails.location(),
                                currentDetails.website(),
                                currentDetails.birthDate(),
                                currentDetails.avatarUrl(),
                                fileUrl
                        );
                    }

                    if (detailsStorage.updateAccountDetails(updatedDetails)) {
                        if (isAjax) {
                            // Для AJAX запроса возвращаем JSON ответ
                            resp.setContentType("application/json");
                            resp.setCharacterEncoding("UTF-8");
                            resp.getWriter().write("{\"success\": true, \"message\": \"Image uploaded successfully\"}");
                        } else {
                            session.setAttribute("flash_success", "Image uploaded successfully");
                            resp.sendRedirect(req.getContextPath() + "/profile");
                        }
                    } else {
                        if (isAjax) {
                            resp.setContentType("application/json");
                            resp.setCharacterEncoding("UTF-8");
                            resp.getWriter().write("{\"success\": false, \"message\": \"Failed to save image information\"}");
                        } else {
                            session.setAttribute("flash_error", "Failed to save image information");
                            resp.sendRedirect(req.getContextPath() + "/profile");
                        }
                    }
                } else {
                    if (isAjax) {
                        resp.setContentType("application/json");
                        resp.setCharacterEncoding("UTF-8");
                        resp.getWriter().write("{\"success\": false, \"message\": \"Failed to upload image\"}");
                    } else {
                        session.setAttribute("flash_error", "Failed to upload image");
                        resp.sendRedirect(req.getContextPath() + "/profile");
                    }
                }
            } else {
                if (isAjax) {
                    resp.setContentType("application/json");
                    resp.setCharacterEncoding("UTF-8");
                    resp.getWriter().write("{\"success\": false, \"message\": \"No file selected\"}");
                } else {
                    session.setAttribute("flash_error", "No file selected");
                    resp.sendRedirect(req.getContextPath() + "/profile");
                }
            }
        } catch (Exception e) {
            if (isAjax) {
                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF-8");
                resp.getWriter().write("{\"success\": false, \"message\": \"Error uploading file: \" + e.getMessage()}");
            } else {
                session.setAttribute("flash_error", "Error uploading file: " + e.getMessage());
                resp.sendRedirect(req.getContextPath() + "/profile");
            }
            e.printStackTrace();
        }
    }

    private String saveFile(Part filePart, String fileName, HttpServletRequest request) {
        try {
            String appPath = request.getServletContext().getRealPath("");
            String uploadPath = appPath + File.separator + UPLOAD_DIR;

            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            File file = new File(uploadDir, fileName);
            try (InputStream input = filePart.getInputStream()) {
                Files.copy(input, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }

            return fileName;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getFileName(Part part) {
        String contentDisposition = part.getHeader("content-disposition");
        String[] tokens = contentDisposition.split(";");
        for (String token : tokens) {
            if (token.trim().startsWith("filename")) {
                return token.substring(token.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return "unknown";
    }

    private String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex).toLowerCase();
    }
}