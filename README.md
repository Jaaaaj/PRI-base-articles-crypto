
# PRI-base-articles-crypto 2021/2022

## Présentation du projet

Nous sommes la troisième équipe à travailler sur ce sujet. Notre travail s'est appuyé sur l'application réalisée par le premier groupe qui a travaillé sur ce sujet que nous avons fiabilisé et dont nous avons étendu les fonctionnalités. Leur travail peut être consulté sur [leur github](https://github.com/RaphaelChevasson/PRI-base-articles-crypto).

  

Equipe étudiante: Amine BOUZID, Ruben FELICIANO, Jinda WU

Client: Pierre-Louis CAYREL

Tuteur: Christophe GRAVIER

  

## Notre travail

Les fonctionnalités que nous avons ajoutées au produit sont les suivantes :

* La connexion entre les boutons de l'interface et l'API du côté serveur (qui n'avait pas été fait avant, sans doute par manque de temps).

* L'ajout de nouvelles sources pour rechercher des publications. Pour cela, nous utilisons l'[API](https://www.semanticscholar.org/product/api) de [Semantic Scholar](https://www.semanticscholar.org/), un site regroupant les publications scientifiques provenant de pluiseurs sources, telles que ArXiv, IEEE ou IACR EPrint. Cela nous permet d'obtenir une plus grande variété d'articles avec une recherche.

* L'ajout de **mots clés** qui sont utilisés lors des requêtes de recherche d'article afin d'en affiner le résultat. Cela nous a permit de régler le principal problème qui nous a été remonté par rapport à l'application précédente, qui était le manque de précision des résultats (on récupérait tous les articles du site d'[Hal Inria](https://hal.inria.fr/), peut importait leur sujet).

* La mise en place d'un fichier de configuration (appelé *keywords.txt* présent dans */server/src/main/ressources*) qui permet à un utilisateur de lister des mots clés pertinents sur lesquelle il veut faire une recherche. Une recherche peut contenir plusieurs mots clés séparés pas un **+**. Pour recherche une expression exacte, il faudra entourer cette expression avec des **""**. Il est également possible d'avoir une liste de keywords (et de faire autant de recherches) en les séparant par une **virgule** et un **saut de ligne**.

* La possibilité d'exporter les articles présents dans notre base de données au format **Bibtex** depuis l'interface de l'application.

* La partie machine n'a pas pu être traitée par manque de temps
  

# Tutoriel d'installation
## From source

  

Clone this repository:

  

```

git clone https://github.com/Jaaaaj/PRI-base-articles-crypto

```

  

### BackEnd :

  

First, you need to install a MySQL server (we suggest [MySQL Workbench](https://dev.mysql.com/downloads/workbench/)) 

The first step is to create a database named : pri_database.

You have to run your database server before running the application so you can connect it to the database.

Then, all you have to do is to open the maven project named as server on your IDE and run ServerApplication.java as a springboot application.

You can modify MySQL Login information and ports in *application.properties* file in the server project. 

Finally on your browser go to : http://localhost:8080/ to check if server is running.

#### Keywords :
The API calls use Keywords that can be found in *keywords.txt* present in the following directory */server/src/main/ressources*. 

    "Code-based cryptography",
    McEliece+Goppa+Code+Quasi-Dyadic,
    Diﬀerential Power Analysis+ McEliece Cryptosystem+ QC-MDPC Codes,

Each line needs to end with a "," (comma). Seperate multiple keywords by "+". Or use quotes to use the words as a single query keyword.





  

### FrontEnd :
First, you need to install [NodeJs](https://nodejs.org/en/download/)

In the front folder, open a Terminal and run the following commands to install dependencies :

```

npm install

```

and the you start the Angular devserver :

```

npm start

```

Finally on your browser go to : http://localhost:4200/

  
--------------------------------------

# Machine Learning (old version - Not tested)

**The following instruction are from the 2018-2019 original project. This part has not been tested or fixed.**

### Setup Extraction

The admin page is located at : http://localhost:4200/admin
It allows you to start the extraction, or to reset the database 

To start the extraction, you click on the Start search button like in the picture down bellow :

![](.github/web_admin_interface.png)

If you want to reset the database, you click on the Reset database button.

If you want to see the total number of the extracted posts,the creation date, the last modified date and if the service running, you will have to refresh the page.

### Article results

To see all the posts extracted you have to go to : http://localhost:4200/posts

You will find a search form that allows you to filter depending on the Title, Author or Keywords. 

You can also export the article metada as a BibTex file:

 - The button ***Export All Posts To Bibtex*** will export all existing articles in the database
 - The button ***Export this page to BibTex*** will only export the current page articles

  
  

### Setup Classification

  

Follow the setup steps in [the classification readme](Classification/readme.md)

  

# Usage

  

## Launch the Extraction-Classification-Storage pipeline

  

Launch `launch_pipeline.sh` on a terminal. If you are on windows, lauch it using [git bash](https://gitforwindows.org/)

  

## Browse the list

  

## Use the administration interface

  

Go to : http://localhost:4200/admin/edit

You will have the list of all the posts and you can edit or delete them.

  

# How it works

  

Here is a little diagram showing essential bricks of the project, the main technology they use, and how they connect together:

  

![](.github/diagram.png)

  

# Inquiries

  

Feel free to contact us via github!
