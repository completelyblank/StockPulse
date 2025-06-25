import { Routes } from '@angular/router';
import { Register } from './components/register/register';
import { Login } from './components/login/login';
import { Stocks } from './components/stocks/stocks';
import { Portfolio } from './components/portfolio/portfolio';
import { Leaderboard } from './components/leaderboard/leaderboard';
import { Profile } from './components/profile/profile';
import { Dividends } from './components/dividends/dividends';
import { StockDetail } from './components/stock-detail/stock-detail';
import { NewsComponent } from './components/news/news';

export const routes: Routes = [
  { path: 'register', component: Register },
  { path: 'login', component: Login },
  { path: 'profile', component: Profile },
  { path: 'stocks', component: Stocks },
  { path: 'dividends', component: Dividends },
  { path: 'portfolio', component: Portfolio },
  { path: 'leaderboard', component: Leaderboard },
  { path: 'stocks/:symbol', component: StockDetail },
  { path: 'news', component: NewsComponent },

  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: '**', redirectTo: '/login' }  // Wildcard route for 404
];
