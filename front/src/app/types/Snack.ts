export interface Snack {
    title: string;
    type: 'success' | 'error' | 'info';
    duration?: number;
    action?: string;
}