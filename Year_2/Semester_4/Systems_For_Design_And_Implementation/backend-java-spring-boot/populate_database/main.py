from delete.delete_books import delete_data_books
from delete.delete_libraries import delete_data_libraries
from delete.delete_memberships import delete_data_memberships
from delete.delete_readers import delete_data_readers
from insert.insert_books import insert_data_books
from insert.insert_libraries import insert_data_libraries
from insert.insert_memberships import insert_data_memberships
from insert.insert_readers import insert_data_readers
from insert.insert_user_profiles import insert_data_user_profiles
from insert.insert_user_roles import insert_data_user_roles
from insert.insert_users import insert_data_users

if __name__ == "__main__":
    # insert_data_user_profiles()
    # insert_data_users()
    # insert_data_user_roles()
    # insert_data_libraries()
    # insert_data_books()
    # insert_data_readers()
    insert_data_memberships()
