// src/app/components/candlestick.ts
import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { environment } from '../../environments/environment';

declare var CanvasJS: any;

@Component({
  selector: 'app-candlestick',
  standalone: true,
  imports: [CommonModule],
  template: `<div id="chartContainer" style="height: 400px; width: 100%;"></div>`,
})
export class CandlestickComponent implements OnInit {
  apiKey = environment.polygonAPI;

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.fetchCandlestickData();
  }

  fetchCandlestickData() {
    const url = `https://api.polygon.io/v2/aggs/ticker/AAPL/range/1/day/2024-05-01/2024-06-01?adjusted=true&sort=asc&apiKey=${this.apiKey}`;
    this.http.get<any>(url).subscribe({
      next: (res) => {
        const dataPoints = res.results.map((bar: any) => ({
          x: new Date(bar.t),
          y: [bar.o, bar.h, bar.l, bar.c]
        }));
        this.renderChart(dataPoints);
      },
      error: (err) => {
        console.error('Error fetching candlestick data:', err);
        alert('Could not fetch chart data');
      }
    });
  }

  renderChart(dataPoints: any[]) {
    const chart = new CanvasJS.Chart('chartContainer', {
      title: {
        text: '📉 AAPL Candlestick Chart',
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
