# Steamworks
Code for the FRC Steamworks robot

Getting Started

1. Become a collaborator to the team Github repository.
	A. Go to https://github.com/.
	B. Create an account if you don't have one.
	C. Let Brian know your Github username, text 989-941-9194.
	D. When Brian invites you to become a collaborator, accept.
	* Continue to next steps even if you have not been invited yet.

2. Install Notepad++
	A. Go to https://notepad-plus-plus.org/.
	B. Click Download in the side menu.
	C. Click the Download button.
	D. Run the installer, keeping default options.

3. Install Git
	A. Go to https://git-scm.com/.
	B. Click Downloads.
	C. Click Windows.
	D. Run the installer, keeping default options.

4. Copy the team repository to your computer.
	A. Create a folder to hold the repository (e.g. C:\Users\JohnDoe\Documents\Git Repositories\).
	B. Right-click in the folder, select Git Bash Here.
	C. Run the following commands. These only have to be done once. You can copy-paste, excluding the brackets and changing necessary fields.
		a. Run [git config --global user.name "John Doe"]. Change John Doe to your name.
		b. Run [git config --global user.email johndoe@email.com]. Change johndoe@email.com to your email.
		c. Run [git config --global core.editor "'C:/Program Files (x86)/Notepad++/notepad++.exe' -multiInst -nosession"].
	D. Run [git clone https://github.com/Team815/Steamworks.git].

-----

Git Instructions

First time use:
	git config --global user.name "John Doe"
	git config --global user.email johndoe@email.com
	git config --global core.editor "'C:/Program Files (x86)/Notepad++/notepad++.exe' -multiInst -nosession"

Get (clone) an existing repository
	git clone https://github.com/Team815/Steamworks.git

Add (or update) files in the repository
	git add file.txt

Commit changes locally
	git commit

Push changes to master
	git push

See the current status of the local repository
	git status