CREATE OR REPLACE FUNCTION create_account_details()
RETURNS TRIGGER AS $$
BEGIN
INSERT INTO account_details (account_id)
VALUES (NEW.id);
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER after_account_insert
    AFTER INSERT ON accounts
    FOR EACH ROW
    EXECUTE FUNCTION create_account_details();