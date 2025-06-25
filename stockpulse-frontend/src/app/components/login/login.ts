import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { UserService } from '../../services/user';
import { ToastNotificationComponent } from '../toast-notification/toast';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, ToastNotificationComponent],
  templateUrl: './login.html',
  styleUrls: ['./login.css']
})
export class Login {
  username = '';
  password = '';
  viewInitialized: boolean = false;
    toastVisible = false;
    toastMessage = '';
    toastType: 'success' | 'error' | 'info' = 'info';

  constructor(private userService: UserService, private router: Router) {
    
  }

   showToast(msg: string, type: 'success' | 'error' | 'info' = 'info') {
        this.toastMessage = msg;
        this.toastType = type;
        this.toastVisible = true;
        setTimeout(() => this.toastVisible = false, 3500);
    }

  onLogin() {
  this.userService.login(this.username, this.password).subscribe({
    next: (response) => {
      console.log('Login success:', response);
      this.showToast('✅ Login successful!', 'success');
      localStorage.setItem('user', JSON.stringify(response));
      this.router.navigate(['/profile']);
    },
    error: (err) => {
      console.error('Login failed:', err);
      this.showToast('❌ Login failed: ' + (err.error || 'Check credentials'), 'error');
    }
  });
}
}