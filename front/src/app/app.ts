import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Navbar } from "./shared/navbar/Navbar";
import { ToastContainerComponent } from './shared/toastContainer/ToastContainer';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, Navbar, ToastContainerComponent],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('front');
}
