import { check } from 'k6';
import http from 'k6/http';

export default function () {
    const res = http.get('http://localhost:8080/sleep/platform?duration=30');

    check(res, {
        'is status 200': (r) => r.status === 200,
    });
}