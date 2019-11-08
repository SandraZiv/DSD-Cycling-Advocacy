# How to run the backend?
I kindly suggest you to use virtual environments, this is to avoid mixing up projects and requirements

To create the virtual environment use:

    python3 -m venv env
    
To activate the virtual environment use:

    source env/bin/activate
    
All of the following commands **MUST** be executed from inside the virtual environment

To install the required libraries use:

    pip install -r app/requirements.txt

To start the backend use:

    python run.py

To deactivate the virtual environment use:

    deactivate