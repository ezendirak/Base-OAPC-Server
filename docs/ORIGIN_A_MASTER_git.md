Comprobación y sincronizacion de origin con master
==================================================

Rápido
------
```
git fetch origin master
git pull origin master
```


Ver ramas
---------
Vemos las **ramas** definidas con `git branch`. 

*master* es la rama local y *origin* la que esta en Github.
```
D:\oapc-app\oapc-server>git branch
* master
  origin/master
```

Estado sincronización
---------------------
Realizamos la **comprobacion** de la sincronización con `git fetch origin master`
```
D:\oapc-app\oapc-server>git fetch origin master
remote: Counting objects: 3, done.
remote: Compressing objects: 100% (3/3), done.
remote: Total 3 (delta 2), reused 0 (delta 0), pack-reused 0
Unpacking objects: 100% (3/3), done.
From https://github.com/xpoveda/oapc-server
 * branch            master     -> FETCH_HEAD
   aae14f5..f5bdd55  master     -> origin/master
```

El sistema nos indica con `git status` el **estado** de *master* vs *origin*.

La rama *master* (local) esta desactualizada con la *origin*.

No tenemos nada que subir de *master* a *origin*.
```
D:\oapc-app\oapc-server>git status
On branch master
Your branch is behind 'remotes/origin/master' by 2 commits, and can be fast-forwarded.
  (use "git pull" to update your local branch)

nothing to commit, working tree clean
```

Podemos ver las **diferencias** entre las dos ramas con `git diff master origin`
```
D:\oapc-app\oapc-server>git diff master origin
warning: refname 'origin/master' is ambiguous.
diff --git a/README.md b/README.md
index 120ee72..8b13789 100644
--- a/README.md
+++ b/README.md
```

Con `git log` podemos ver los **hashes** los diferentes **commits** que han habido en *master*.

Con `git log origin` los de *origin*.
```
D:\oapc-app\oapc-server>git log
commit 4b18b02a26a176b811ba7e23e7b2b19970ce5f2e (HEAD -> master)
Author: xpoveda <xpoveda@gmail.com>
Date:   Thu Feb 22 12:18:22 2018 +0100

    Update README.md
```

Sincronizado del origin al master
---------------------------------
Volcamos los cambios de *origin* a *master* con `git pull origin master`
```
D:\oapc-app\oapc-server>git pull origin master
From https://github.com/xpoveda/oapc-server
 * branch            master     -> FETCH_HEAD
Updating 4b18b02..f5bdd55
Fast-forward
 README.md | 52 ++++++++++++++++++++++++++++++++++++----------------
 1 file changed, 36 insertions(+), 16 deletions(-)
```

Ahora si que estamos sincronizados con el *origin*
```
D:\oapc-app\oapc-server>git fetch origin master
From https://github.com/xpoveda/oapc-server
 * branch            master     -> FETCH_HEAD
 
 D:\oapc-app\oapc-server>git pull origin master
From https://github.com/xpoveda/oapc-server
 * branch            master     -> FETCH_HEAD
Already up-to-date.

D:\oapc-app\oapc-server>git status
On branch master
Your branch is up-to-date with 'remotes/origin/master'.

nothing to commit, working tree clean

```
