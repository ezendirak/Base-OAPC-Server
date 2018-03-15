
Subida inicial a Github y descarga inicial en local
===================================================

Subida inicial a github
=======================
Para el server
```
Hacer antes un "make clean"
crear repo en github "oapc-server"
git init
Con tortoisegit hacer un add de todos los fuentes con "Include ignored files" activo
git commit -m "first commit"
git remote add origin https://github.com/xpoveda/oapc-server.git
git push -u origin master
```

Para el front
-------------
```
Eliminar antes "node_modules" o no añadirlos al hacer el "add"
crear repo en github "oapc-front"
git init
Con tortoisegit hacer un add de todos los fuentes con "Include ignored files" activo
git commit -m "first commit"
git remote add origin https://github.com/xpoveda/oapc-front.git
git push -u origin master
```

Descarga inicial en local
=========================
```shell
D:\oapc-app>git clone https://github.com/xpoveda/oapc-server.git
Cloning into 'oapc-server'...
remote: Counting objects: 65, done.
remote: Compressing objects: 100% (56/56), done.
Uremote: Total 65 (delta 1), reused 61 (delta 0), pack-reused 0
Unpacking objects: 100% (65/65), done.

D:\oapc-app>git clone https://github.com/xpoveda/oapc-front.git
Cloning into 'oapc-front'...
remote: Counting objects: 75, done.
remote: Compressing objects: 100% (52/52), done.
remote: Total 75 (delta 13), reused 71 (delta 12), pack-reused 0
Unpacking objects: 100% (75/75), done.
```
