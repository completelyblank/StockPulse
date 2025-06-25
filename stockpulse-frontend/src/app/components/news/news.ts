// src/app/components/news/news.component.ts
import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule, DatePipe } from '@angular/common';
import { environment } from '../../environments/environment';

@Component({
  selector: 'app-news',
  templateUrl: './news.html',
  imports: [CommonModule],
  styleUrls: ['./news.css']
})
export class NewsComponent implements OnInit {
  articles: any[] = [];
  loading = true;
  error = '';

  constructor(private http: HttpClient) {}

  ngOnInit() {
    const apiKey = environment.newsApI;
    const url = `https://newsapi.org/v2/everything?q=stock+market&language=en&sortBy=publishedAt&pageSize=10&apiKey=${apiKey}`;

    this.http.get<any>(url).subscribe({
      next: (res) => {
        this.articles = res.articles;
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Failed to load news';
        this.loading = false;
        console.error(err);
      }
    });
  }
}
