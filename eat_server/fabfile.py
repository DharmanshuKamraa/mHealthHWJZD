from fabric.api import *
from fabric.contrib.project import rsync_project

env.project_name = 'eat' # use parent folder name?

# env.db_user = 'root'
# env.db_password = ''
env.db_host = 'localhost'
env.db_name = env.project_name
env.use_ssh_config = True
env.use_ssh_config = True
env.remote_app_dir = '/home/ubuntu/eat/'

env.roledefs.update({
    # 'webServer': ['hungr'],
    'devServer' : ['proxi']
})

@roles('devServer')
def migrate_dev():
    with cd(env.remote_app_dir) :
        run('source venv/bin/activate')
        run('fab migrate')

@roles('webServer')
def migrate_prod():
    with cd(env.remote_app_dir) :
        sudo('source venv/bin/activate')
        sudo('fab migrate')

@roles('devServer')
def rsync_dev():
    rsync_project(
            remote_dir=env.remote_app_dir,
            local_dir='*',
            exclude=('venv/*' , 'eat/manage.py' , '*.tar' , '*.gz' , 'docs/*' , 'design_assets/*' , '*.sh' , '*.sql' , '*.pyc'),
            # delete=True,
            # extra_opts="--dry-run"
            )

    # run('sudo service supervisor stop')
    # run('sudo service supervisor start')

@roles('webServer')
def rsync():
    rsync_project(
            remote_dir=env.remote_app_dir,
            local_dir='*',
            exclude=('venv/*' , 'proxi/manage.py' , '*.tar' , '*.gz' , 'docs/*' , 'design_assets/*' , '*.sh' , '*.sql' , '*.pyc'),
            # delete=True,
            # extra_opts="--dry-run"
            )

    # run('sudo service supervisor stop')
    # run('sudo service supervisor start')

def test():
    local('source venv/bin/activate'  , shell='/bin/bash')

def setup(db=False):
    '''Use this to setup the project for the first time after cloning the project.'''
    local('virtualenv venv')
    install_requirements()
    if(db):
        create_db()
    manage('migrate')

def install_requirements():
    local('pip install -r requirements/local.txt')

def new_migration():
    manage('schemamigration core --auto')
    migrate()

def migrate():
    manage('migrate')

def db_sync():
    manage('syncdb')

def devserver():
    manage('runserver')


def manage(cmd):
    print env.project_name
    print cmd
    local('source ../venv/bin/activate && python {project_name}/manage.py {cmd}'.format(
            project_name=env.project_name,
            cmd=cmd
        ) , shell="/bin/bash")
