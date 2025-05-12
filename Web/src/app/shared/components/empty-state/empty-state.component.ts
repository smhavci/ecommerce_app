import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-empty-state',
  standalone: false,
  templateUrl: './empty-state.component.html',
  styleUrl: './empty-state.component.css'
})
export class EmptyStateComponent {
  @Input() message: string = 'Görüntülenecek veri yok.';
}
