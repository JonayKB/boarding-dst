import { Routes } from '@angular/router';
import { CarsView } from './features/CarsView';

export const routes: Routes = [
    {
        path: '',
        redirectTo: 'cars',
        pathMatch: 'full'
    }
    ,
    {
        path: 'cars',
        component: CarsView
    }
];