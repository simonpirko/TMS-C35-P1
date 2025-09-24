<%--
  Created by IntelliJ IDEA.
  User: Laptop
  Date: 28.08.2025
  Time: 12:45
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<html>
<head>
    <title>${account.username()} - Profile</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-sRIl4kxILFvY47J16cr9ZwB07vP4J8+LH7qKQnuqkuIAvNWLzeN8tE5YBujZqJLB" crossorigin="anonymous">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/profile-page-style.css">
    <style>
        .profile-avatar {
            width: 150px;
            height: 150px;
            border-radius: 50%;
            object-fit: cover;
            border: 4px solid #fff;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }
        .profile-header {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            padding: 40px 0;
            margin-bottom: 30px;
        }
        .stats-number {
            font-size: 1.5rem;
            font-weight: bold;
            color: #333;
        }
        .stats-label {
            font-size: 0.9rem;
            color: #666;
        }
        .nav-tabs .nav-link {
            padding: 15px 20px;
            font-weight: 500;
            color: #555;
            border: none;
            border-bottom: 3px solid transparent;
        }
        .nav-tabs .nav-link.active {
            color: #0d6efd;
            background: transparent;
            border-bottom: 3px solid #0d6efd;
        }
        .nav-tabs .nav-link:hover {
            border-color: transparent;
            border-bottom: 3px solid #dee2e6;
        }
        .btn-edit {
            border-radius: 20px;
            padding: 8px 20px;
            font-weight: 500;
        }
    </style>
</head>
<body>
<!-- Сообщения об успехе/ошибке -->
<c:if test="${not empty sessionScope.success}">
    <div class="alert alert-success alert-dismissible fade show m-3" role="alert">
            ${sessionScope.success}
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    </div>
    <c:remove var="success" scope="session"/>
</c:if>
<c:if test="${not empty sessionScope.error}">
    <div class="alert alert-danger alert-dismissible fade show m-3" role="alert">
            ${sessionScope.error}
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    </div>
    <c:remove var="error" scope="session"/>
</c:if>

<jsp:include page="_header.jsp"/>

<div class="profile-header"
     style="<c:choose>
     <c:when test='${not empty accountDetails.headerUrl()}'>
             background: url('${pageContext.request.contextPath}${accountDetails.headerUrl()}') center/cover;
     </c:when>
     <c:otherwise>
             background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
     </c:otherwise>
             </c:choose>">
    <div class="container">
        <div class="row align-items-center">
            <div class="col-md-2 text-center">
                <!-- Аватарка из accountDetails или дефолтная -->
                <c:choose>
                    <c:when test="${not empty accountDetails.avatarUrl()}">
                        <img src="${pageContext.request.contextPath}${accountDetails.avatarUrl()}"
                             alt="${account.username()}" class="profile-avatar">
                    </c:when>
                    <c:otherwise>
                        <img src="https://ui-avatars.com/api/?name=${account.username()}&background=random&size=150&rounded=true&format=svg"
                             alt="${account.username()}" class="profile-avatar">
                    </c:otherwise>
                </c:choose>
            </div>
            <div class="col-md-6">
                <h1 class="text-white mb-2">${account.username()}</h1>
                <!-- Био из accountDetails если есть -->
                <c:if test="${not empty accountDetails.bio()}">
                    <p class="text-white mb-1">${accountDetails.bio()}</p>
                </c:if>
                <p class="text-white-50 mb-0">@${account.username()}</p>
                <!-- Дополнительная информация если есть -->
                <c:if test="${not empty accountDetails.location()}">
                    <p class="text-white-50 mb-0">
                        <i class="bi bi-geo-alt"></i> ${accountDetails.location()}
                    </p>
                </c:if>
                <c:if test="${not empty accountDetails.website()}">
                    <p class="text-white-50 mb-0">
                        <i class="bi bi-link-45deg"></i>
                        <a href="${accountDetails.website()}" class="text-white-50" target="_blank">${accountDetails.website()}</a>
                    </p>
                </c:if>
                <c:if test="${not empty accountDetails.gender()}">
                    <p class="text-white-50 mb-0">
                        <span class="text-white-50" >
                               ♂♀ ${accountDetails.gender()}
                        </span>
                    </p>
                </c:if>
            </div>
            <div class="col-md-4 text-end">
                <c:if test="${sessionScope.account.id() == account.id()}">
                    <button class="btn btn-light btn-edit" data-bs-toggle="modal" data-bs-target="#editProfileModal">
                        <i class="bi bi-pencil me-2"></i>Edit Profile
                    </button>
                </c:if>
                <c:if test="${not empty currentUser && currentUser.id() != account.id()}">
                    <form action="${pageContext.request.contextPath}/check-profile" method="post" class="d-inline">
                        <input type="hidden" name="action" value="${isFollowing ? 'unfollow' : 'follow'}">
                        <input type="hidden" name="profileId" value="${account.id()}">
                        <button type="submit" class="btn ${isFollowing ? 'btn-outline-primary' : 'btn-primary'} rounded-pill px-4">
                            <c:choose>
                                <c:when test="${isFollowing}">
                                    <i class="bi bi-person-check me-2"></i>Following
                                </c:when>
                                <c:otherwise>
                                    <i class="bi bi-person-plus me-2"></i>Follow
                                </c:otherwise>
                            </c:choose>
                        </button>
                    </form>
                </c:if>
            </div>
        </div>
    </div>
</div>

<div class="container">
    <div class="row mb-4">
        <div class="col-md-8 mx-auto">
            <div class="row text-center">
                <div class="col-4">
                    <div class="stats-number">${not empty postCount ? postCount : 0}</div>
                    <div class="stats-label">Posts</div>
                </div>
                <div class="col-4">
                    <div class="stats-number">${not empty followingCount ? followingCount : 0}</div>
                    <div class="stats-label">Following</div>
                </div>
                <div class="col-4">
                    <div class="stats-number">${not empty followersCount ? followersCount : 0}</div>
                    <div class="stats-label">Followers</div>
                </div>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="col-md-8 mx-auto">
            <ul class="nav nav-tabs justify-content-center mb-4" id="profileTabs" role="tablist">
                <li class="nav-item" role="presentation">
                    <button class="nav-link active" id="posts-tab" data-bs-toggle="tab"
                            data-bs-target="#posts" type="button" role="tab" aria-controls="posts"
                            aria-selected="true">
                        <i class="bi bi-chat-text me-2"></i>Posts
                    </button>
                </li>
                <li class="nav-item" role="presentation">
                    <button class="nav-link" id="likes-tab" data-bs-toggle="tab"
                            data-bs-target="#likes" type="button" role="tab" aria-controls="likes"
                            aria-selected="false">
                        <i class="bi bi-heart me-2"></i>Likes
                    </button>
                </li>
            </ul>

            <div class="tab-content" id="profileTabContent">
                <div class="tab-pane fade show active" id="posts" role="tabpanel" aria-labelledby="posts-tab">
                    <c:choose>
                        <c:when test="${not empty posts}">
                            <div class="row">
                                <c:forEach var="post" items="${posts}">
                                    <div class="col-12 mb-3">
                                        <div class="card">
                                            <div class="card-body">
                                                <p class="card-text">${post.content()}</p>
<%--                                                <div class="d-flex justify-content-between align-items-center">--%>
<%--                                                    <small class="text-muted">--%>
<%--                                                        <i class="bi bi-clock me-1"></i>--%>
<%--                                                            ${post.createdAt()}--%>
<%--                                                    </small>--%>
<%--                                                    <div>--%>
<%--                                                        <span class="text-muted me-3">--%>
<%--                                                            <i class="bi bi-heart me-1"></i>${post.likesCount()}--%>
<%--                                                        </span>--%>
<%--                                                        <span class="text-muted">--%>
<%--                                                            <i class="bi bi-chat me-1"></i>${post.commentsCount()}--%>
<%--                                                        </span>--%>
<%--                                                    </div>--%>
<%--                                                </div>--%>
                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="text-center py-5">
                                <i class="bi bi-chat-text display-4 text-muted mb-3"></i>
                                <p class="text-muted">No posts yet</p>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>

                <div class="tab-pane fade" id="likes" role="tabpanel" aria-labelledby="likes-tab">
                    <div class="text-center py-5">
                        <i class="bi bi-heart display-4 text-muted mb-3"></i>
                        <p class="text-muted">No liked posts yet</p>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Модальное окно для редактирования профиля -->
<c:if test="${sessionScope.account.id() == account.id()}">
    <div class="modal fade" id="editProfileModal" tabindex="-1" aria-labelledby="editProfileModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">
                <div class="modal-header border-0 pb-0">
                    <div class="d-flex align-items-center w-100">
                        <!-- Название окна -->
                        <h5 class="modal-title fs-5 fw-bold me-auto">Edit profile</h5>

                        <!-- Крестик -->
                        <button type="button" class="btn-close ms-3" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                </div>

                <div class="modal-body">
                    <!-- Шапка профиля -->
                    <div class="mb-4 position-relative">
                        <div class="profile-header-edit position-relative"
                             style="height: 150px; border-radius: 12px;
                             <c:choose>
                             <c:when test="${not empty accountDetails.headerUrl()}">
                                     background: url('${pageContext.request.contextPath}${accountDetails.headerUrl()}') center/cover;
                             </c:when>
                             <c:otherwise>
                                     background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                             </c:otherwise>
                                     </c:choose>">
                            <button class="btn btn-dark position-absolute top-50 start-50 translate-middle rounded-pill"
                                    style="opacity: 0.9;">
                                <i class="bi bi-camera me-2"></i>Edit header
                            </button>
                            <input type="file" class="d-none" id="headerImage" accept="image/*">
                        </div>

                        <!-- Аватарка профиля -->
                        <div class="position-absolute" style="bottom: -60px; left: 20px;">
                            <div class="position-relative">
                                <c:choose>
                                    <c:when test="${not empty accountDetails.avatarUrl()}">
                                        <img src="${pageContext.request.contextPath}${accountDetails.avatarUrl()}"
                                             alt="${account.username()}"
                                             class="profile-avatar-edit"
                                             style="width: 120px; height: 120px; border-radius: 50%; object-fit: cover;">
                                    </c:when>
                                    <c:otherwise>
                                        <img src="https://ui-avatars.com/api/?name=${account.username()}&background=random&size=120&rounded=true&format=svg"
                                             alt="${account.username()}"
                                             class="profile-avatar-edit"
                                             style="width: 120px; height: 120px; border-radius: 50%; object-fit: cover;">
                                    </c:otherwise>
                                </c:choose>
                                <button class="btn btn-dark btn-sm position-absolute bottom-0 end-0 rounded-circle d-flex align-items-center justify-content-center"
                                        style="width: 36px; height: 36px; opacity: 0.9;">
                                    <i class="bi bi-camera fs-6"></i>
                                    <input type="file" class="d-none" id="avatarImage" accept="image/*">
                                </button>
                            </div>
                        </div>
                    </div>

                    <!-- Форма редактирования -->
                    <div class="mt-5 pt-3">

                        <!-- Основная форма редактирования профиля -->
                        <form action="${pageContext.request.contextPath}/profile" method="post">
                            <input type="hidden" name="action" value="updateProfile">

                            <!-- Email -->
                            <div class="mb-4">
                                <label for="email" class="form-label fw-bold text-muted mb-2">Email</label>
                                <input type="email" class="form-control form-control-lg border-0 bg-light rounded-3"
                                       id="email" name="email"
                                       value="${not empty accountDetails.email() ? accountDetails.email() : ''}"
                                       placeholder="your.email@example.com">
                            </div>

                            <!-- Gender -->
                            <div class="mb-4">
                                <label class="form-label fw-bold text-muted mb-2">Gender</label>
                                <div>
                                <input  type="radio"
                                       id="male" name="gender" value="Man"  required>
                                <label  class="form-check form-check-inline bg-light rounded-3 p-2" > ♂ Man </label>
                                <input  type="radio"
                                       id=" female" name="gender" value="Woman" required>
                                <label  class="form-check form-check-inline bg-light rounded-3 p-2"> ♀ Woman </label>


                            </div>
                            </div>

                            <!-- Bio -->
                            <div class="mb-4">
                                <label for="bio" class="form-label fw-bold text-muted mb-2">Bio</label>
                                <textarea class="form-control border-0 bg-light rounded-3" id="bio" name="bio" rows="3"
                                          placeholder="Tell us about yourself..." style="resize: none;">${not empty accountDetails.bio() ? accountDetails.bio() : ''}</textarea>
                            </div>


                            <!-- Location -->
                            <div class="mb-4">
                                <label for="location" class="form-label fw-bold text-muted mb-2">Location</label>
                                <input type="text" class="form-control border-0 bg-light rounded-3" id="location" name="location"
                                       value="${not empty accountDetails.location() ? accountDetails.location() : ''}"
                                       placeholder="Add location">
                            </div>

                            <!-- Website -->
                            <div class="mb-4">
                                <label for="website" class="form-label fw-bold text-muted mb-2">Website</label>
                                <input type="url" class="form-control border-0 bg-light rounded-3" id="website" name="website"
                                       value="${not empty accountDetails.website() ? accountDetails.website() : ''}"
                                       placeholder="https://example.com">
                            </div>

                            <!-- Дата рождения -->
                            <div class="mb-4">
                                <label for="birthDate" class="form-label fw-bold text-muted mb-2">Birth date</label>
                                <input type="date" class="form-control border-0 bg-light rounded-3" id="birthDate" name="birthDate"
                                       value="${not empty accountDetails.birthDate() ? accountDetails.birthDate() : ''}">
                            </div>

                            <!-- Кнопка Save -->
                            <div class="d-flex justify-content-end mt-4 pt-2">
                                <button type="submit" class="btn btn-primary rounded-pill px-4">Save Profile</button>
                            </div>
                        </form>

                        <!-- Форма для смены username -->
                        <div class="card mb-4">
                            <div class="card-header">
                                <h6 class="mb-0">Change Username</h6>
                            </div>
                            <div class="card-body">
                                <form action="${pageContext.request.contextPath}/profile" method="post">
                                    <input type="hidden" name="action" value="updateUsername">

                                    <div class="mb-3">
                                        <label for="newUsername" class="form-label fw-bold text-muted mb-2">Username</label>
                                        <input type="text" class="form-control form-control-lg border-0 bg-light rounded-3"
                                               id="newUsername" name="newUsername"
                                               value="${account.username()}" required>
                                        <small class="text-muted">Enter your new username</small>
                                    </div>

                                    <div class="mb-3">
                                        <label for="currentPasswordForUsername" class="form-label fw-bold text-muted mb-2">Current Password</label>
                                        <input type="password" class="form-control form-control-lg border-0 bg-light rounded-3"
                                               id="currentPasswordForUsername" name="currentPassword" required>
                                        <small class="text-muted">Enter your current password to confirm changes</small>
                                    </div>

                                    <div class="d-flex justify-content-end">
                                        <button type="submit" class="btn btn-primary rounded-pill px-4">Update Username</button>
                                    </div>
                                </form>
                            </div>
                        </div>

                        <!-- Форма для смены password -->
                        <div class="card mt-4">
                            <div class="card-header">
                                <h6 class="mb-0">Change Password</h6>
                            </div>
                            <div class="card-body">
                                <form action="${pageContext.request.contextPath}/profile" method="post">
                                    <input type="hidden" name="action" value="updatePassword">

                                    <div class="mb-3">
                                        <label for="currentPassword" class="form-label fw-bold text-muted mb-2">Current Password</label>
                                        <input type="password" class="form-control form-control-lg border-0 bg-light rounded-3"
                                               id="currentPassword" name="currentPassword" required>
                                    </div>

                                    <div class="mb-3">
                                        <label for="newPassword" class="form-label fw-bold text-muted mb-2">New Password</label>
                                        <input type="password" class="form-control form-control-lg border-0 bg-light rounded-3"
                                               id="newPassword" name="newPassword" required>
                                    </div>

                                    <div class="mb-3">
                                        <label for="confirmPassword" class="form-label fw-bold text-muted mb-2">Confirm New Password</label>
                                        <input type="password" class="form-control form-control-lg border-0 bg-light rounded-3"
                                               id="confirmPassword" name="confirmPassword" required>
                                    </div>

                                    <div class="d-flex justify-content-end">
                                        <button type="submit" class="btn btn-primary rounded-pill px-4">Update Password</button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</c:if>

<script>
    // Обработчики для загрузки изображений
    document.querySelector('.profile-header-edit').addEventListener('click', function() {
        document.getElementById('headerImage').click();
    });

    document.querySelector('.profile-header-edit button').addEventListener('click', function(e) {
        e.stopPropagation();
        document.getElementById('headerImage').click();
    });

    document.querySelector('.position-relative .btn-sm').addEventListener('click', function(e) {
        e.stopPropagation();
        document.getElementById('avatarImage').click();
    });

    // Обработка выбора файлов
    document.getElementById('headerImage').addEventListener('change', function(e) {
        if (e.target.files.length > 0) {
            if (validateImageFile(e.target.files[0])) {
                uploadFile(e.target.files[0], 'header');
            }
        }
    });

    document.getElementById('avatarImage').addEventListener('change', function(e) {
        if (e.target.files.length > 0) {
            if (validateImageFile(e.target.files[0])) {
                uploadFile(e.target.files[0], 'avatar');
            }
        }
    });

    function validateImageFile(file) {
        const allowedTypes = ['image/jpeg', 'image/jpg', 'image/png', 'image/gif', 'image/webp'];
        const maxSize = 5 * 1024 * 1024; // 5MB

        if (!allowedTypes.includes(file.type)) {
            alert('Please select a valid image file (JPEG, PNG, GIF, WebP)');
            return false;
        }

        if (file.size > maxSize) {
            alert('File size must be less than 5MB');
            return false;
        }

        return true;
    }

    function uploadFile(file, type) {
        const formData = new FormData();
        formData.append('type', type);
        formData.append('file', file);

        const uploadButton = type === 'avatar'
            ? document.querySelector('.position-relative .btn-sm')
            : document.querySelector('.profile-header-edit button');

        const originalHtml = uploadButton.innerHTML;
        uploadButton.innerHTML = '<i class="bi bi-arrow-clockwise spin"></i> Uploading...';
        uploadButton.disabled = true;

        fetch('${pageContext.request.contextPath}/upload', {
            method: 'POST',
            body: formData
        })
            .then(response => {
                if (response.ok) {
                    return response.text();
                }
                throw new Error('Upload failed');
            })
            .then(() => {
                location.reload();
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Error uploading image: ' + error.message);
                uploadButton.innerHTML = originalHtml;
                uploadButton.disabled = false;
            });
    }

    // Валидация форм
    document.querySelectorAll('form').forEach(form => {
        // Валидация username
        if (form.querySelector('input[name="newUsername"]')) {
            form.addEventListener('submit', function(e) {
                const newUsername = form.querySelector('input[name="newUsername"]').value;

                if (newUsername.length < 3) {
                    e.preventDefault();
                    alert('Username must be at least 3 characters long');
                    return false;
                }

                if (!/^[a-zA-Z0-9_]+$/.test(newUsername)) {
                    e.preventDefault();
                    alert('Username can only contain letters, numbers, and underscores');
                    return false;
                }

                const currentPassword = form.querySelector('input[name="currentPassword"]').value;
                if (!currentPassword) {
                    e.preventDefault();
                    alert('Current password is required');
                    return false;
                }
            });
        }

        // Валидация password
        if (form.querySelector('input[name="newPassword"]')) {
            form.addEventListener('submit', function(e) {
                const newPassword = form.querySelector('input[name="newPassword"]').value;
                const confirmPassword = form.querySelector('input[name="confirmPassword"]').value;

                if (newPassword !== confirmPassword) {
                    e.preventDefault();
                    alert('Passwords do not match');
                    return false;
                }

                if (newPassword.length < 6) {
                    e.preventDefault();
                    alert('Password must be at least 6 characters long');
                    return false;
                }

                const currentPassword = form.querySelector('input[name="currentPassword"]').value;
                if (!currentPassword) {
                    e.preventDefault();
                    alert('Current password is required');
                    return false;
                }
            });
        }
    });
    // Автоматическое скрытие alert через 5 секунд
    setTimeout(() => {
        document.querySelectorAll('.alert').forEach(alert => {
            new bootstrap.Alert(alert).close();
        });
    }, 2000);
</script>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-FKyoEForCGlyvwx9Hj09JcYn3nv7wiPVlz7YYwJrWVcXK/BmnVDxM+D2scQbITxI"
        crossorigin="anonymous">
</script>

<script>
    // Активация табов
    const triggerTabList = document.querySelectorAll('#profileTabs button')
    triggerTabList.forEach(triggerEl => {
        new bootstrap.Tab(triggerEl)
    })
</script>
</body>
</html>