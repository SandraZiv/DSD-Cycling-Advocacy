from functools import wraps
import jsonschema
from flask import current_app, jsonify, request


##	This function is a wrapper that will allow to check if the data from the user is a json.
#	@param f The function which has to be decorated.
def validate_json(f):
    @wraps(f)
    def wrapper(*args, **kw):
        try:
            request.json
        except:
            return jsonify({'success': False, 'error': 'payload must be a valid json', 'code': 1}), 400
        return f(*args, **kw)
    return wrapper


def validate_schema(schema_name):
    def decorator(f):
        @wraps(f)
        def wrapper(*args, **kw):
            try:
                schema = current_app.config[schema_name]
                jsonschema.validate(request.json, schema, format_checker=jsonschema.FormatChecker())
            except Exception as e:
                return jsonify({'success': False, 'error': str(e), 'code': 2}), 400
            return f(*args, **kw)
        wrapper._schema_name = schema_name
        return wrapper
    return decorator
