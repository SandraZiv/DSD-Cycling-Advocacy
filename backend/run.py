#!/usr/bin/env python
# -*- coding: utf-8 -*-
from werkzeug.serving import run_simple
from app import app
from flask import send_from_directory


@app.route('/')
def root():
    return send_from_directory('static', 'index.html')


@app.route('/favicon.ico')
def favicon():
    return send_from_directory('static', 'favicon.ico')


@app.route('/static/<path:filename>')
def serve_static(path, filename):
    return send_from_directory(path, filename)


@app.after_request
##	The after function is called after a request has been sent from the server to the client.
def after(response):
    response.direct_passthrough = False
    return response


if __name__ == '__main__':
    run_simple('localhost', 5000, app, use_reloader=True)
