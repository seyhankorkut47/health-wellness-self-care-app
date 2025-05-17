from flask import Blueprint, request
from db_config import get_db_connection

insert_reminders = Blueprint('insert_reminders', __name__)

@insert_reminders.route("/insert_reminders", methods=["POST"])
def insert_reminders_route():
    user_email = request.form.get("user_email", "")
    exercise_time = request.form.get("exercise_time", "")
    exercise_enabled = request.form.get("exercise_enabled", "false")
    water_time = request.form.get("water_time", "")
    water_enabled = request.form.get("water_enabled", "false")
    sleep_time = request.form.get("sleep_time", "")
    sleep_enabled = request.form.get("sleep_enabled", "false")

    if user_email and exercise_time and water_time and sleep_time:
        conn = get_db_connection()
        cursor = conn.cursor()

        cursor.execute("DELETE FROM reminders WHERE user_email = %s", (user_email,))
        sql = """
            INSERT INTO reminders 
            (user_email, exercise_time, exercise_enabled, water_time, water_enabled, sleep_time, sleep_enabled)
            VALUES (%s, %s, %s, %s, %s, %s, %s)
        """
        values = (
            user_email,
            exercise_time,
            1 if exercise_enabled == "true" else 0,
            water_time,
            1 if water_enabled == "true" else 0,
            sleep_time,
            1 if sleep_enabled == "true" else 0
        )

        try:
            cursor.execute(sql, values)
            conn.commit()
            return "success"
        except Exception as e:
            print("Insert error:", e)
            return "fail"
        finally:
            cursor.close()
            conn.close()
    else:
        return "fail"
