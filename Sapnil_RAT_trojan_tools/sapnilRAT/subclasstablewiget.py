#!/usr/bin/env python

import sys
from PyQt5.QtCore import Qt
from PyQt5.QtGui import *
from PyQt5 import QtCore, QtGui, QtWidgets
from PyQt5.QtWidgets import QTableWidgetItem,QMenu
from PyQt5.QtCore import Qt

class TableWidget(QtWidgets.QTableWidget):

    def __init__(self, parent = None):
    
        QtWidgets.QTableWidget.__init__(self, parent)
    
    def contextMenuEvent(self, event):
    
        menu = QMenu(self)
        newAct = menu.addAction("New")
        openAct = menu.addAction("Open")
        quitAction = menu.addAction("Quit")
       
       
        action = menu.exec_(self.mapToGlobal(event.pos()))
        if action == quitAction:
            qApp.quit()


