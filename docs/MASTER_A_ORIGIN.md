Sincronizacion master con origin
================================
Rápido
------
```
git status
git add -A
git commit -m "mensaje"
git push -u origin master
```

Comprobamos el estado con `git status`
```
D:\oapc-app\oapc-server>git status
On branch master
Your branch is up-to-date with 'remotes/origin/master'.

Changes not staged for commit:
  (use "git add/rm <file>..." to update what will be committed)
  (use "git checkout -- <file>..." to discard changes in working directory)

        deleted:    SINCRONIZACION_git.md
        deleted:    SUBIDA_DESCARGA_INICIAL_git.md

Untracked files:
  (use "git add <file>..." to include in what will be committed)

        docs/

no changes added to commit (use "git add" and/or "git commit -a")
```

Añadimos todo con `git add -A`
```
D:\oapc-app\oapc-server>git add -A

D:\oapc-app\oapc-server>git status
On branch master
Your branch is up-to-date with 'remotes/origin/master'.

Changes to be committed:
  (use "git reset HEAD <file>..." to unstage)

        renamed:    SINCRONIZACION_git.md -> docs/SINCRONIZACION_git.md
        renamed:    SUBIDA_DESCARGA_INICIAL_git.md -> docs/SUBIDA_DESCARGA_INICIAL_git.md

```

Hacemos commit en master con `git commit -m`
```
D:\oapc-app\oapc-server>git commit -m "cambios en master suben a origin"
[master 59c3f3f] cambios en master suben a origin
 2 files changed, 0 insertions(+), 0 deletions(-)
 rename SINCRONIZACION_git.md => docs/SINCRONIZACION_git.md (100%)
 rename SUBIDA_DESCARGA_INICIAL_git.md => docs/SUBIDA_DESCARGA_INICIAL_git.md (100%)
```

Actualizamos el *origin* con el *master* con `git push -u origin master`
```
D:\oapc-app\oapc-server>git push -u origin master
Counting objects: 3, done.
Delta compression using up to 4 threads.
Compressing objects: 100% (3/3), done.
Writing objects: 100% (3/3), 372 bytes | 74.00 KiB/s, done.
Total 3 (delta 1), reused 0 (delta 0)
remote: Resolving deltas: 100% (1/1), completed with 1 local object.
To https://github.com/xpoveda/oapc-server.git
   4897b19..59c3f3f  master -> master
Branch master set up to track remote branch master from origin.
```

```
D:\oapc-app\oapc-server>git status
On branch master
Your branch is up-to-date with 'remotes/origin/master'.

nothing to commit, working tree clean
```
