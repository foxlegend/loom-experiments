import { check } from 'k6';
import http from 'k6/http';

export const options = {
    discardResponseBodies: true,
    scenarios: {
        cpu: {
            executor: 'constant-vus',
            exec: 'cpu',
            vus: 100,
            duration: '1m'
        },
        sleep: {
            executor: 'constant-vus',
            exec: 'sleep',
            vus: 100,
            duration: '1m'
        }
    }
}

export function cpu() {
    const res = http.get('http://localhost:8080/cpu/platform', {
        tags: { scenario: 'cpu'}
    })
    check(res, {
        'is status 200': (r) => r.status === 200,
    })
}

export function sleep() {
    const res = http.get('http://localhost:8080/sleep/platform', {
        tags: { scenario: 'sleep'}
    })
    check(res, {
        'is status 200': (r) => r.status === 200,
    })
}

export default function() {

}