import os

# --- PATHS ---
ROOT_PATH = os.path.dirname(os.path.abspath(__file__))  # refers to application_top
# DIR_PATH = os.path.join(APP_ROOT, 'directory-name')   # can be used to get any directory path

# --- QUEUE ---
RABBITMQ_HOST = 'localhost'
TRIP_ANALYSIS_QUEUE = 'trip-analysis-queue'
MAP_UPDATE_QUEUE = 'map-update-queue'
TRIP_ANALYSIS_JOB = 'trip-analysis-job'
TEST_JOB = 'test-job'

LOG_FILE = ROOT_PATH+'/log.log'
