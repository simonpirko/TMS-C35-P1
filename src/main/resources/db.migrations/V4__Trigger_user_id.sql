CREATE OR REPLACE FUNCTION set_user_id_from_username()
    RETURNS TRIGGER AS $$
BEGIN

    SELECT ad.account_id
    INTO NEW.user_id
    FROM account_details ad;


    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Триггер
CREATE TRIGGER trg_set_user_id
    BEFORE INSERT ON posts
    FOR EACH ROW
EXECUTE FUNCTION set_user_id_from_username();