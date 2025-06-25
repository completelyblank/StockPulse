import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';

// Define interfaces for typing
interface StockHolding {
  symbol: string;
  quantity: number;
  avgBuyPrice: number;
  currentPrice: number;
  gainLoss: number;
}

interface PortfolioResponse {
  holdings: StockHolding[];
  totalValue: number;
}

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './profile.html',
  styleUrls: ['./profile.css']
})
export class Profile implements OnInit {
  user: any = null;
   portfolio: { holdings: any[]; totalValue: number } = {
    holdings: [],
    totalValue: 0
  };
transactions: any[] = [];
  userId: string = '';
  showHoldings = false;
  showTransactions = false;

  constructor(
    private http: HttpClient,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    const storedUser = localStorage.getItem('user');
    if (storedUser) {
      const parsed = JSON.parse(storedUser);
      this.userId = parsed.id;
      this.fetchProfile(this.userId);
      this.fetchPortfolio(this.userId);
      this.fetchTransactions(this.userId);
    } else {
      this.router.navigate(['/login']);
    }
  }

  fetchProfile(id: string) {
    this.http.get(`http://localhost:8081/api/users/${id}`).subscribe({
      next: (data) => this.user = data,
      error: (err) => {
        console.error('Error loading profile:', err);
        alert('Unable to load profile.');
      }
    });
  }

  fetchPortfolio(id: string) {
    this.http.get<PortfolioResponse>(`http://localhost:8081/api/portfolio/${id}`).subscribe({
      next: (data) => this.portfolio = data,
      error: (err) => {
        console.error('Error loading portfolio:', err);
        alert('Unable to load portfolio.');
      }
    });
  }

  fetchTransactions(id: string) {
    this.http.get<any[]>(`http://localhost:8081/api/transactions/${id}`).subscribe({
      next: (data) => this.transactions = data,
      error: (err) => {
        console.error('Error loading transactions:', err);
        alert('Unable to load transactions.');
      }
    });
  }
}
