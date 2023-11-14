import { check } from 'k6';
import http from 'k6/http';

export const options = {
    stages: [
        { target: 300, duration: '2m'},
        { target: 300, duration: '20s'},
    ]
}

export default function () {
    const res = http.get('http://localhost:8080/uuid/virtual');

    check(res, {
        'is status 200': (r) => r.status === 200,
    });
}