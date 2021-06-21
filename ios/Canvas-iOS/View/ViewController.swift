//
//  ViewController.swift
//  Canvas-iOS
//
//  Created by Lam Nguyen on 21/06/2021.
//

import UIKit

class ViewController: UIViewController {
    
    let canvas = Canvas()
    
    override func loadView(){
        self.view = canvas
    }

    override func viewDidLoad() {
        super.viewDidLoad()
        
        canvas.backgroundColor = UIColor.getColor(hex: "baby_blue")
        
    }


}

