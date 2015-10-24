

blank server name when creating tomcat:
http://crunchify.com/eclipse-how-to-fix-installing-apache-tomcat-server-issue-blank-server-name-field/

wrong server configuration:
http://lucasterdev.altervista.org/wordpress/2012/05/12/could-not-load-the-tomcat-server-configuration-at-serverstomcat-v7-0-server-at-localhost-config-the-configuration-may-be-corrupt-or-incomplet/


integrating tomcat with eclipse:
     – Run the following commands in terminal

    cd ~/workspace/.metadata/.plugins/org.eclipse.core.runtime/.settings/
    rm org.eclipse.jst.server.tomcat.core.prefs
    rm org.eclipse.wst.server.core.prefs
    cd /usr/share/tomcat7
    sudo service tomcat7 stop
    sudo update-rc.d tomcat8 disable
    sudo ln -s /var/lib/tomcat8/conf conf
    sudo ln -s /etc/tomcat8/policy.d/03catalina.policy conf/catalina.policy
    sudo ln -s /var/log/tomcat8 log
    sudo chmod -R 777 /usr/share/tomcat8/conf
    sudo ln -s /var/lib/tomcat8/common common
    sudo ln -s /var/lib/tomcat8/server server
    sudo ln -s /var/lib/tomcat8/shared shared

    – Restart eclipse
    – In Project Explorer of Eclipse, you can see ‘Servers’. Right click and delete it.
    – Re-add the Server (File -> New -> Other -> Server)
    – Now your project on Eclipse should run fine. 
