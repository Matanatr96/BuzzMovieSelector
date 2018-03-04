IMPORTANT: IF YOU ARE EVALUATING UI PLEASE CHECKOUT BRANCH M9. BUILD.GRADLE FOR JUNITS DOES NOT HAVE A COMPATIBLE SDK FOR OUR FRONTEND
-------
This is from JT on the command line

Commands for working in github:
~~~
git checkout branchname
~~~
Add your code and stuff
~~~
git add .
git commit -m "Describe your changes"
git checkout master
git pull
git checkout branchname
git merge master
~~~
Fix any merge conflicts
-------
Open any files that have merge conflict in a text editor (You can find them by typing "git status").
It should have lines that look something like :
~~~
 <<<<<<< HEAD:file
 here's some stuff
 =======
 here's some different stuff
 >>>>>>> 4e2b407f501b68f8588aa645acafffa0224b9b78:file
~~~
Delete all the lines with <<<<<< and >>>>>>>, and pick either the stuff above or below the ====== line to keep and then delete the other stuff as well as the ====== line. **IMPORTANT** Make sure that everything still runs just fine before continuing forward, including compiling and running all code.
Then when you're done do the following to save:
~~~
git add .
git commit -m "Fixed dem merge conflicts"
git push
git checkout master
git merge branchname
git push
~~~
(If you had no merge conflicts just do the last 3 lines)
M2 Git Use
-------
For M2, since we're all editing different files, I'll allow pushing and pulling directly from master. There are a few slight differences when using it though
~~~
git add .
~~~
should be changed to 
~~~
git add filename
~~~
to avoid weird stuff happening with iml and local.properties files. Make sure you add all the files and only the files that you have changed and want saved. Then type 
~~~
git status
~~~
and you might see some red files. If they're untracked files, either add them or delete them completely. If they're modified files that you don't care about your changes, type 
~~~
git checkout filename
~~~
and then when you're done do 
~~~
git commit -m "Description of changes"
git pull
git push
~~~
and then make sure your file(s) have been added to the main page
Gradle help
--------
https://docs.gradle.org/current/userguide/tutorial_using_tasks.html
https://docs.gradle.org/current/userguide/java_plugin.html
