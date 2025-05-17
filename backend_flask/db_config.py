# db_config.py
import MySQLdb

def get_db_connection():
    return MySQLdb.connect(
        host="localhost",
        user="root",
        passwd="",
        db="wellness_app"
    )
