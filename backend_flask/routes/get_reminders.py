# routes/get_reminders.py
from flask import Blueprint, request, jsonify
from db_config import get_db_connection

get_reminders = Blueprint('get_reminders', __name__)

@get_reminders.route('/get_reminders', methods=['GET'])
def get_reminders_func():
    email = request.args.get("email")

    if not email:
        return jsonify({"error": "Missing email"}), 400

    db = get_db_connection()
    cursor = db.cursor()
    cursor.execute("SELECT exercise_time, exercise_enabled, water_time, water_enabled, sleep_time, sleep_enabled FROM reminders WHERE user_email = %s", (email,))
    result = cursor.fetchone()

    if result:
        return jsonify({
            "exercise_time": str(result[0]),
            "exercise_enabled": bool(int(result[1])),
            "water_time": str(result[2]),
            "water_enabled": bool(int(result[3])),
            "sleep_time": str(result[4]),
            "sleep_enabled": bool(int(result[5]))
        })
    else:
        return jsonify({})
