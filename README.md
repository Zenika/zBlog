# Blog-backend

## Requirements

### Install vagrant

Download and install package from [Vagrant](http://downloads.vagrantup.com/)

### Install librarian-puppet

    sudo apt-get install ruby-dev

    sudo gem install librarian-puppet puppet

### Download puppet module

    librarian-puppet install

### Boot & provision the VM

    vagrant up

### Install RestX

    curl -s http://restx.io/install.sh | sh

## Start application

### Via RestX shell

    restx

Launch Application

    app run

enter 'stop' to stop the running application

### Via IDE

Start the main from org.blog.AppServer

Remember to follow documentation from [Restx.io](http://restx.io/docs/ide.html) to handle IDE integration
