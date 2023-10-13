# Arkama Plugin manager
LOGO MEDIAPI



Arkama est un projet de serveur minecraft minijeux/ survie/ creatif, aujourd'hui abandonné il a été repris pour le developpement d'un serveur privé au sein de l'organisation mediapi.org

## Dépendance principales 
- [paper](https://papermc.io) : un framework de serveur minecraft, il sert a faire des serveur et les plugins doit donc etre creer avec leurs propre librarie
- [maven](https://maven.apache.org/) : une librairie qui permet de faciliter la compilation java avec des fichiers
- [Arkama-Core](oui) : coeur du projet il permet de centraliser le plus important de tout les plugins confondu

## structure du projet

```markdown
.
├── data
│   └── contient tout les plugins
│
├── documentation
│
├── envs
│   └──dev
│
└── scripts

```


## installation 

### prérequis

#### installation de maven et java
avant toutes chose il vous faut les libraries necessaire pour le fonctionnement global
##### sur linux
1. mettez a jour l'index de packet 
```shell
sudo apt-get update
```

2. installez java
```shell
sudo apt-get java
```
3. installez maven
```shell
sudo apt-get maven
```

##### sur macos
1. installez les package par brew
```shell
brew install java
brew install maven
```

##### compilation des plugins
pour récuperer les plugins en jar il faut exectuer le script suivant :
```bash
bash scripts/compilate.sh
```
ce script creera dans la racine du repository le directory ``plugins`` avec les fichiers en jar



