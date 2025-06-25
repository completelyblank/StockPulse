import { AfterViewInit, Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ToastNotificationComponent } from '../toast-notification/toast';

declare var CanvasJS: any;

@Component({
    selector: 'app-stock-detail',
    standalone: true,
    imports: [FormsModule, CommonModule, ToastNotificationComponent],
    templateUrl: './stock-detail.html',
    styleUrls: ['./stock-detail.css']
})
export class StockDetail implements OnInit, AfterViewInit {
    symbol: string = '';
    stockName: string = '';
    latestPrice: number = 0;
    candlestickData: any[] = [];
    quantity: number = 1;
    userId: string = '';
    apiKey: string = environment.polygonAPI;
    viewInitialized: boolean = false;
    toastVisible = false;
    toastMessage = '';
    toastType: 'success' | 'error' | 'info' = 'info';

    showToast(msg: string, type: 'success' | 'error' | 'info' = 'info') {
        this.toastMessage = msg;
        this.toastType = type;
        this.toastVisible = true;
        setTimeout(() => this.toastVisible = false, 3500);
    }
    constructor(private route: ActivatedRoute, private http: HttpClient) { }

    ngOnInit(): void {
        this.symbol = this.route.snapshot.params['symbol'];
        const user = localStorage.getItem('user');
        if (user) {
            this.userId = JSON.parse(user).id;
        }

        this.fetchStockInfo();
        this.fetchCandlestick();
    }

    ngAfterViewInit(): void {
        this.viewInitialized = true;
        if (this.candlestickData.length > 0) {
            this.renderChart();
        }
    }

    fetchStockInfo() {
        const metaUrl = `https://api.polygon.io/v3/reference/tickers/${this.symbol}?apiKey=${this.apiKey}`;
        const priceUrl = `https://api.polygon.io/v2/aggs/ticker/${this.symbol}/prev?adjusted=true&apiKey=${this.apiKey}`;

        this.http.get<any>(metaUrl).subscribe({
            next: (res) => {
                this.stockName = res?.results?.name || this.symbol;
            },
            error: () => {
                this.stockName = this.symbol;
            }
        });

        this.http.get<any>(priceUrl).subscribe({
            next: (res) => {
                if (Array.isArray(res?.results) && res.results.length > 0) {
                    this.latestPrice = res.results[0].c || 0;
                } else {
                    this.latestPrice = 0;
                }
            },
            error: () => {
                this.latestPrice = 0;
            }
        });
    }

    fetchCandlestick() {
        const url = `https://api.polygon.io/v2/aggs/ticker/${this.symbol}/range/1/day/2024-01-01/2024-06-01?adjusted=true&sort=asc&apiKey=${this.apiKey}`;
        this.http.get<any>(url).subscribe({
            next: (res) => {
                if (Array.isArray(res?.results)) {
                    this.candlestickData = res.results.map((bar: any) => ({
                        x: new Date(bar.t),
                        y: [bar.o, bar.h, bar.l, bar.c]
                    }));
                    if (this.viewInitialized) {
                        this.renderChart();
                    }
                }
            },
            error: (err) => console.error('Error fetching candlestick:', err)
        });
    }

    renderChart() {
        if (!this.viewInitialized || this.candlestickData.length === 0) return;

        const chart = new CanvasJS.Chart("chartContainer", {
            animationEnabled: true,
            backgroundColor: "rgba(0, 0, 0, 0.6)",

            title: {
                text: `${this.symbol} - Candlestick Chart`,
                fontColor: "#8c92ac",
                fontSize: 22,

            },

            axisX: {
                valueFormatString: "DD MMM",
                labelFontColor: "#8c92ac",
                lineColor: "#00ffff44",
                tickColor: "#00ffff88",
                gridColor: "#00ffff22"
            },

            axisY: {
                includeZero: false,
                title: "Price (USD)",
                titleFontColor: "#8c92ac",
                labelFontColor: "#8c92ac",
                lineColor: "#00ffff44",
                tickColor: "#00ffff88",
                gridColor: "#00ffff22"
            },

            toolTip: {
                backgroundColor: "#111",
                borderColor: "#0ff",
                fontColor: "#0ff",
                shared: true
            },

            data: [{
                type: "candlestick",
                risingColor: "#00ff99",
                color: "#ff3366",
                dataPoints: this.candlestickData
            }]
        });

        chart.render();
    }
    buyStock() {
        const payload = {
            userId: this.userId,
            symbol: this.symbol,
            quantity: this.quantity
        };

        this.http.post('http://localhost:8081/api/trade/buy', payload).subscribe({
             next: () => this.showToast('✅ Bought stock successfully!', 'success'),
             error: () => this.showToast('❌ Failed to buy stock!', 'error')
           
        });
    }

    sellStock() {
        const payload = {
            userId: this.userId,
            symbol: this.symbol,
            quantity: this.quantity
        };

        this.http.post('http://localhost:8081/api/trade/sell', payload).subscribe({
            next: () => this.showToast('✅ Sold stock successfully!', 'success'),
             error: () => this.showToast('❌ Failed to sell stock!', 'error')
           
        });
    }
}
