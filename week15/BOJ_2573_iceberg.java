import java.io.*;
import java.util.*;

/*
    빙산의 녹는 조건 : 근처에 연결된 바닷물(0)의 개수만큼 감소
    동서남북 4방향
    빙산이 두 덩어리 이상으로 분리되는 최초의 시간
    전부 녹을때까지 두덩어리가 되지 않으면 0 출력, 
                
    N, M : 3 ~300
    0 이상 10이하
    빙산이 차지하는 칸의 개수는 10,000개 이하

    필요 : 1. 모든 칸을 돌면서, bfs -> 두개인지 검사
    for
        for
            칸돌면서 검사 (1인지)
    
            ---
            녹일 빙산 찾기 => 큐에넣음 (r, c, 녹일 높이)
            -> 연쇄적으로 녹을 수 있으니 마지막에 한번에 녹이기

            검사-> 빙산이 (두개 이상인지)
    
*/ 
public class BOJ_2573_iceberg {

    static int day = 0;
    static int[] dr = { -1, 1, 0, 0 };
    static int[] dc = { 0, 0, -1, 1 };
    static int N;
    static int M;
    static int[][] matrix;
    static int[][] visited;
    public static void main(String[] args) throws Exception {
        //핵심
        if (new File("input.txt").exists()) {
            System.setIn(new FileInputStream("input.txt"));
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        //입력받기
        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        //visited가 필요한가? -> 섬의 연결을 체크할때 필요

        visited = new int[N][M];
        matrix = new int[N][M];

        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < M; j++) {
                matrix[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        int total = 0;
        boolean cant = false;
        while (total <= 1) {
            if (total != 0) {
                day += 1;   
            }
            total = 0;
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < M; j++) {
                    if (matrix[i][j] != 0 && visited[i][j] <= day) {
                        visited[i][j] = day + 1;
                        //빙산녹이기
                        melt(i, j, day);
                        //검사
                        total += 1;
                    }

                }
            }

             if (total == 0) {
                cant = true;
                break;
            }

           
        }

            // System.out.println("-----cant------" + cant);    
        System.out.println(cant ?  0 : day);
    }
    
    static void melt(int r, int c, int currentDay) {

        Queue<int[]> q = new ArrayDeque<>();
        Queue<int[]> subq = new ArrayDeque<>();
        //근처의 좌표 탐색, 녹일 빙산 큐에 담기
        int nr;
        int nc;
        int cnt = check4(r, c);
        //4방향탐색

        q.offer(new int[] { r, c });
        subq.offer(new int[] { r, c, cnt });

        while (!q.isEmpty()) {
            int[] cur = q.poll();
            //연결된 좌표가 0인지, 
            for (int i = 0; i < 4; i++) {
                nr = cur[0] + dr[i];
                nc = cur[1] + dc[i];

                if (oob(nr, nc) || visited[nr][nc] > currentDay)
                    continue;
                if (matrix[nr][nc] == 0)
                    cnt++;

                if (matrix[nr][nc] != 0) {
                    q.offer(new int[] { nr, nc });
                    subq.offer(new int[] { nr, nc, check4(nr, nc) });
                }
                visited[nr][nc] = currentDay + 1;
            }
            cnt = 0;
        }
        
        //큐에 든 값 녹이기 
        while (!subq.isEmpty()) {
            int[] cur = subq.poll();
            int cr = cur[0];
            int cc = cur[1];
            int h = cur[2];

            matrix[cr][cc] = Math.max(matrix[cr][cc] - h, 0);
        
             
        }
    }
    
    static boolean oob(int r, int c) {
        return r < 0 || c < 0 || r >= N || c >= M;
    }

    static int check4(int r, int c) {
        int cnt = 0;

        for (int i = 0; i < 4; i++) {
            int nr = r + dr[i];
            int nc = c + dc[i];
            if (matrix[nr][nc] == 0)
                cnt++;
        }
        return cnt;
    }
}