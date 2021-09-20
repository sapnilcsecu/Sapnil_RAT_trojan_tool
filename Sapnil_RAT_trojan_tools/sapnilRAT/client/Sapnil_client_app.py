'''
Created on Sep 15, 2021

@author: Nasir(programmer)
'''

import sys
from PyQt5 import QtWidgets, uic
from client_builder import Ui_Form

class MainWindow(QtWidgets.QMainWindow, Ui_Form):
    def __init__(self, *args, obj=None, **kwargs):
        super(MainWindow, self).__init__(*args, **kwargs)
        self.setupUi(self)



def on_click(self):
    print('PyQt5 button click')

app = QtWidgets.QApplication(sys.argv)

window = MainWindow()
#window.pushButton.clicked.connect(on_click)
window.show()
app.exec()
