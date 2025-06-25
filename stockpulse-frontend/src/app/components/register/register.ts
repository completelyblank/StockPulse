import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { UserService } from '../../services/user';
import { ToastNotificationComponent } from '../toast-notification/toast';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, FormsModule, ToastNotificationComponent],
  templateUrl: './register.html',
  styleUrls: ['./register.css']
})
export class Register {
  username = '';
  password = '';
  viewInitialized: boolean = false;
    toastVisible = false;
    toastMessage = '';
    toastType: 'success' | 'error' | 'info' = 'info';


  constructor(private userService: UserService) {}

  showToast(msg: string, type: 'success' | 'error' | 'info' = 'info') {
        this.toastMessage = msg;
        this.toastType = type;
        this.toastVisible = true;
        setTimeout(() => this.toastVisible = false, 3500);
    }
  onRegister() {
  this.userService.register(this.username, this.password).subscribe({
    next: (res) => {
      console.log('Registration successful:', res);
      this.showToast('✅ User registered successfully!', 'success');
    },
    error: (err) => {
      console.error('Registration failed:', err);
      this.showToast('❌ Registration failed: ' + (err.error || 'Please try again.'), 'error');
    }
  });
}
}
