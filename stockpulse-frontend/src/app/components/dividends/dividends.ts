// src/app/components/dividends.ts
import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { environment } from '../../environments/environment';

declare var CanvasJS: any;

@Component({
  selector: 'app-dividends',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './dividends.html',
  styleUrls: ['./dividends.css']
})
export class Dividends implements OnInit {
  dividends: any[] = [];
  selectedTicker: string = '';
  apiKey: string = environment.polygonAPI;

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.fetchDividends();
  }

  fetchDividends() {
    const url = `https://api.polygon.io/v3/reference/dividends?apiKey=${this.apiKey}`;
    this.http.get<any>(url).subscribe({
      next: (res) => this.dividends = res.results || [],
      error: (err) => {
        console.error('Failed to fetch dividends:', err);
        alert('Error fetching dividend data');
      }
    });
  }

  loadCandlestick(ticker: string) {
  this.selectedTicker = ticker;
  const url = `https://api.polygon.io/v2/aggs/ticker/${ticker}/range/1/day/2024-05-01/2024-06-01?adjusted=true&sort=asc&apiKey=${this.apiKey}`;

  this.http.get<any>(url).subscribe({
    next: (res) => {
        console.log('Candlestick API response:', res);

      if (!res.results || !Array.isArray(res.results)) {
        alert('No candlestick data found for this ticker.');
        return;
      }

      const dataPoints = res.results.map((bar: any) => ({
        x: new Date(bar.t),
        y: [bar.o, bar.h, bar.l, bar.c]
      }));

      this.renderChart(dataPoints, ticker);
    },
    error: (err) => {
      console.error('Candlestick error:', err);
      alert('Failed to fetch candlestick data');
    }
  });
}


  renderChart(dataPoints: any[], ticker: string) {
    const chart = new CanvasJS.Chart('chartContainer', {
      title: {
        text: `📈 ${ticker} Candlestick Chart`,
        fontFamily: 'Arial'
      },
      zoomEnabled: true,
      exportEnabled: true,
      axisY: {
        includeZero: false,
        title: 'Price ($)'
      },
      axisX: {
        valueFormatString: 'DD MMM',
        labelAngle: -45
      },
      data: [{
        type: 'candlestick',
        risingColor: 'green',
        color: 'red',
        dataPoints: dataPoints
      }]
    });
    chart.render();
  }
}
