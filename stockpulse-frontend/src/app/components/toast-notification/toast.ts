import { Component, Input, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-toast-notification',
  templateUrl: './toast.html',
  styleUrls: ['./toast.css'],
imports: [CommonModule, FormsModule],
  standalone: true
})
export class ToastNotificationComponent implements OnInit {
  @Input() message: string = '';
  @Input() type: 'success' | 'error' | 'info' = 'info';
  visible: boolean = true;

  ngOnInit() {
    setTimeout(() => this.visible = false, 3500);
  }
}
