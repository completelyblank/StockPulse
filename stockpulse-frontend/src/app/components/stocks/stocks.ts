// src/app/components/stocks.ts
import { Component, OnInit, HostListener } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { environment } from '../../environments/environment';

@Component({
  selector: 'app-stocks',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './stocks.html',
  styleUrls: ['./stocks.css']
})
export class Stocks implements OnInit {
  stocks: any[] = [];
  userId: string = '';
  quantity: number = 1;
  searchTerm: string = '';
  apiKey: string = environment.polygonAPI;
  page: number = 1;
  loading: boolean = false;
  endOfList: boolean = false;

  constructor(private http: HttpClient, private router: Router) { }

  ngOnInit(): void {
    const user = localStorage.getItem('user');
    if (user) {
      this.userId = JSON.parse(user).id;
      this.fetchStocks();
    }
  }

  get filteredStocks() {
    return this.stocks.filter(s =>
      s.symbol.toLowerCase().includes(this.searchTerm.toLowerCase())
    );
  }

  navigateToStock(symbol: string) {
    this.router.navigate(['/stocks', symbol]);
  }

  async fetchStocks() {
    if (this.loading || this.endOfList) return;
    this.loading = true;
    const url = `https://api.polygon.io/v3/reference/tickers?market=stocks&active=true&limit=10&page=${this.page}&apiKey=${this.apiKey}`;

    try {
      const metaRes: any = await this.http.get(url).toPromise();
      const tickers = metaRes.results || [];
      if (tickers.length === 0) {
        this.endOfList = true;
        this.loading = false;
        return;
      }

      const dateObj = new Date();
      dateObj.setDate(dateObj.getDate() - 1); // Use yesterday
      const date = dateObj.toISOString().slice(0, 10);

      const groupedUrl = `https://api.polygon.io/v2/aggs/grouped/locale/us/market/stocks/${date}?adjusted=true&apiKey=${this.apiKey}`;
      const priceRes: any = await this.http.get(groupedUrl).toPromise();

      const prices: Record<string, number> = {};
      for (let r of priceRes.results || []) {
        prices[r.T] = r.c;
      }

      const newStocks = tickers.map((t: any) => ({
        symbol: t.ticker,
        name: t.name,
        price: prices[t.ticker] || 0
      }));

      this.stocks.push(...newStocks);
      this.page++;
    } catch (err: any) {
      console.error('Failed to load stocks:', err);
    } finally {
      this.loading = false;
    }
  }

  @HostListener('window:scroll', [])
  onScroll(): void {
    if ((window.innerHeight + window.scrollY) >= document.body.offsetHeight - 100) {
      this.fetchStocks();
    }
  }
}
