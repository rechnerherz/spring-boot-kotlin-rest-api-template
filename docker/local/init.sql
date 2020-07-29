# Grant user 'example' access to all databases starting with 'example'
GRANT ALL PRIVILEGES ON `example%`.* TO 'example'@'%' IDENTIFIED BY 'example';
FLUSH PRIVILEGES;
