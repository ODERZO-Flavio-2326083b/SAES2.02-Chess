# Jeu d'Échecs en JavaFX
## Introduction
Ce projet consiste à développer une application de jeu d'échecs utilisant JavaFX. Elle permet de jouer dans plusieurs modes : contre un bot, en 1v1 sur une seule machine, ou en tournoi à élimination directe. L'application inclut aussi des fonctionnalités pour gérer les profils des joueurs et leurs statistiques, ainsi que pour sauvegarder et reprendre des parties.

## Fonctionnalités
1. Modes de jeu
• Contre un bot : Jouez contre un adversaire contrôlé par l'ordinateur qui effectue des mouvements aléatoires.
• 1v1 sur la même machine : Deux joueurs peuvent s'affronter en alternant les tours sur un même appareil.
• Tournoi : Organisez des tournois à élimination directe entre plusieurs participants.

2. Limite de temps
• Les joueurs peuvent définir une limite de temps pour chaque partie. Si le temps imparti est dépassé, une pièce sera déplacée aléatoirement.

3. Gestion des profils
• Enregistrez les profils des joueurs avec leur pseudonyme, nombre de parties jouées et gagnées.

4. Consultation des statistiques et de l'historique
• Consultez les statistiques des joueurs et l'historique des parties précédentes. 



## Utilisation
1. Page d'accueil
• Sélectionnez l'un des trois modes de jeu :
• Contre un bot
• 1v1 sur la même machine
• Tournoi

2. Contre un bot
• Entrez votre pseudonyme et chargez votre profil, puis cochez la case pour choisir un combat contre un bot.
• Le bot jouera des coups aléatoires.

3. 1v1 sur la même machine
• Entrez les pseudonymes des deux joueurs et chargez leurs profils respectifs.
• Définissez une limite de temps en haut à droite de la fenêtre.
• Alternez les tours entre les joueurs.

4. Tournoi
• Entrez les pseudonymes de tous les participants.
• Configurez et lancez le tournoi, en précisant les noms de chaque joueur séparés par des virgules dans la zone de texte prévue à cet effet. 
• Notez que chaque joueur doit avoir joué au moins une fois en 1 contre 1 pour avoir un fichier de joueur crée au préalable, sinon, le joueur sera ignoré.

5. Consultation des statistiques
• Accédez à l'onglet "Parties" pour voir les statistiques des joueurs (nombre de parties jouées et gagnées).  Vous pouvez double-cliquer sur une entrée dans le tableau pour obtenir des statistiques détaillées sur le joueur.
• Accédez à l'onglet "Historique" pour consulter les détails des parties précédentes.

## Gestion du projet 
Application codée en JavaFX, développée par BELZ Matteo, FABRE Alexandre, SEBAA Ayrton, ODERZO Flavio.