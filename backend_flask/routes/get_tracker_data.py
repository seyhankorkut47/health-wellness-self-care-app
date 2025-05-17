from flask import Blueprint, request, jsonify
from db_config import get_db_connection

get_tracker_data = Blueprint('get_tracker_data', __name__)

@get_tracker_data.route('/get_tracker_data', methods=['GET'])
def get_data():
    user_email = request.args.get("user_email")
    if not user_email:
        return jsonify([])

    db = get_db_connection()
    cursor = db.cursor()
    cursor.execute("SELECT sleep_hours, water_intake, exercised FROM tracker_data WHERE user_email = %s", (user_email,))
    rows = cursor.fetchall()

    result = []
    for row in rows:
        result.append({
            "sleep_hours": row[0],
            "water_intake": row[1],
            "exercised": bool(row[2])
        })

    return jsonify(result)
