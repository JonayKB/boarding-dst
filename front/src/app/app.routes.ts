import { Routes } from '@angular/router';
import { CarsView } from './features/CarsView';
import { LoginView } from './features/LoginView';
import { SecuredRoute } from './core/guard/AuthGuard';

export const routes: Routes = [
    {
        path: '',
        redirectTo: 'cars',
        pathMatch: 'full'
    },
    {
        path: 'cars',
        component: CarsView,
        canActivate: [SecuredRoute]
    },
    {
        path: 'login',
        component: LoginView
    }
];