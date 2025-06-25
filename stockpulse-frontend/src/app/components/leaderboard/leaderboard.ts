import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-leaderboard',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './leaderboard.html',
  styleUrls: ['./leaderboard.css']
})
export class Leaderboard implements OnInit {
  leaderboard: any[] = [];

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.http.get<any[]>('http://localhost:8081/api/leaderboard').subscribe({
      next: (data) => this.leaderboard = data,
      error: (err) => {
        console.error('Failed to load leaderboard:', err);
        alert('Unable to load leaderboard.');
      }
    });
  }
}
