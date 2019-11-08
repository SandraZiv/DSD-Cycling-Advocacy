from functools import wraps
import mongodb_interface
from flask import current_app, jsonify, request
import bcrypt


def login_required(f):
    @wraps(f)
    def wrapper(*args, **kw):
        if request.authorization:
            username = request.authorization.get('username')
            password = request.authorization.get('password')
            user_data = mongodb_interface.get_user_by_username(username)
            if user_data is None:
                log = [request.remote_addr, request.user_agent, username]
                current_app.logger.info('Invalid user from {} device {} user {}'.format(*log))
                return jsonify({'success': False, 'error': 'invalid user', 'code': 101}), 400
            if not user_data['active']:
                log = [request.remote_addr, request.user_agent, username]
                current_app.logger.info('Not active from {} device {} user {}'.format(*log))
                return jsonify({'success': False, 'error': 'not active', 'code': 102}), 400
            if bcrypt.hashpw(password.encode('utf-8'), user_data['password']) != user_data['password']:
                log = [request.remote_addr, request.user_agent, username]
                current_app.logger.info('Wrong password from {} device {} user {}'.format(*log))
                return jsonify({'success': False, 'error': 'invalid password', 'code': 103}), 400
            current_app.logger.info('User {} requested {}'.format(user_data['_id'], request.path))
        else:
            current_app.logger.info('Missing auth from {} device {}'.format(request.remote_addr, request.user_agent))
            return jsonify({'success': False, 'error': 'missing auth', 'code': 100}), 400
        return f(user_data, *args, **kw)
    return wrapper


def check_permissions(file, level):
    def decorator(f):
        @wraps(f)
        def wrapper(user_data, *args, **kw):
            if file not in user_data['permissions'] or user_data['permissions'][file] < level:
                log = [request.remote_addr, request.user_agent, user_data['username'], file]
                current_app.logger.info('Access denied from {} device {} user {} file {}'.format(*log))
                return jsonify({'success': False, 'error': 'permission denied', 'code': 104}), 400
            return f(user_data, *args, **kw)
        return wrapper
    return decorator
