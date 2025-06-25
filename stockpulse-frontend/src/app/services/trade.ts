import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../environments/environment';

export interface TradeRequest {
  userId: string;
  symbol: string;
  quantity: number;
}

@Injectable({ providedIn: 'root' })
export class TradeService {
  private baseUrl = '/api/trade';

  constructor(private http: HttpClient) {}

  buy(trade: TradeRequest): Observable<any> {
    return this.http.post(`${this.baseUrl}/buy`, trade);
  }

  sell(trade: TradeRequest): Observable<any> {
    return this.http.post(`${this.baseUrl}/sell`, trade);
  }
}
