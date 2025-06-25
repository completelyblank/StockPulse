import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { NewsComponent } from './components/news/news'; // Adjust path as needed
import { Register } from './components/register/register'; // Adjust path
import { Login } from './components/login/login'; // Adjust path
import { Stocks } from './components/stocks/stocks'; // Assuming a component exists
import { Profile } from './components/profile/profile'; // Assuming a component exists
import { Leaderboard } from './components/leaderboard/leaderboard'; // Adjust path

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    RouterModule, // Import RouterModule for routerLink and router-outlet
    NewsComponent,
    Register,
    Login,
    Stocks,
    Profile,
    Leaderboard
  ],
  templateUrl: './app.html', // Adjust to match your template file
  styleUrls: ['./app.css']
})
export class App {}