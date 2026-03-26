import { Routes } from '@angular/router';
import { CarsView } from './features/cars/find-all/CarsView';
import { LoginView } from './features/auth/login/LoginView';
import { isAuthenticated, requiredRole } from './core/guard/auth.guard';
import { ForbiddenView } from './features/auth/forbidden/ForbiddenView';
import { AddCarView } from './features/cars/add-car/AddCarView';
import { GetCarView } from './features/cars/get-car/GetCarView';
import { PutCarView } from './features/cars/put-car/PutCarView';
import { DeleteCarView } from './features/cars/delete-car/DeleteCarView';
import { NotFoundView } from './features/auth/not-found/NotFoundView';

export const routes: Routes = [
    {
        path: '',
        redirectTo: 'cars',
        pathMatch: 'full'
    },
    {
        path: 'cars',
        canActivate: [isAuthenticated()],
        children: [
            {
                path: '',
                component: CarsView,
            },
            {
                path: 'add',
                component: AddCarView,
                canActivate: [requiredRole('ROLE_ADMIN')]
            },
            {
                path: ':id',
                component: GetCarView,
                canActivate: [isAuthenticated()]
            },
            {
                path: ':id/edit',
                component: PutCarView,
                canActivate: [isAuthenticated(), requiredRole('ROLE_ADMIN')]
            },
            {
                path: ':id/delete',
                component: DeleteCarView,
                canActivate: [isAuthenticated(), requiredRole('ROLE_ADMIN')]
            }]
    },
    {
        path: 'forbidden',
        component: ForbiddenView
    },
    {
        path: 'login',
        component: LoginView
    },
    { path: '**', component: NotFoundView }

];