import java.io.*;
import java.util.*;

public class Main {

    static int[][] tomatoBox;
    static int N;
    static int M;
    static int day;

    static final int[] dr = new int[] { -1, 1, 0, 0 };
    static final int[] dc = new int[] { 0, 0, -1, 1 };
    static boolean[][] visited;

    public static void main(String[] args) throws Exception {
        //핵심
        if (new File("input.txt").exists()) {
            System.setIn(new FileInputStream("input.txt"));
        }

        /* 토마토
        익은상태 : 1, 안익은 상태 : 0, 토마토 없음 : -1
        최초 시작시 토마토 모두 익어있으면 0,
        토마토가 모두 익지 못하는 케이스면 -1
        
        토마토의 익힘 : 상하좌우 토마토가 인접해있으면 하루마다 익음
        
        결론 : 토마토가 모두 익는 최소 날짜. 
        
        */

        /*구조
        
        
        
        토마토 체크 - 익었으면 0, 익었으면 1, -> 0base로 시작하기 익는 기준은 다음날
        토마토 번짐
        */
        ArrayDeque<int[]> q = new ArrayDeque<>();
        init(q);
        while (!q.isEmpty()) {

            bfs(q);
            
            if (q.size()== 0) break;
            day++;

        }
       
        System.out.println(tomatoCheck(q) == 1 ? day : -1);
    }

    static void init(Queue<int[]> q) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        M = Integer.parseInt(st.nextToken());
        N = Integer.parseInt(st.nextToken());
        tomatoBox = new int[N][M];
        visited = new boolean[N][M];

        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < M; j++) {
                tomatoBox[i][j] = Integer.parseInt(st.nextToken());
                if (tomatoBox[i][j] == 1)
                    q.offer(new int[] { i, j });
            }
        }
    }

    //전체 토마토가 익었는지 반환
    static int tomatoCheck(ArrayDeque<int[]> q) {
        int redTomato = 0;
        int greenTomato = 0;

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                if (tomatoBox[i][j] == 1) {
                    if (!visited[i][j])
                        q.offer(new int[] { i, j });
                    redTomato += 1;
                } else if (tomatoBox[i][j] == 0) {
                    greenTomato += 1;
                }
            }
        }
        //토마토가 모두 익은 상황
        //모두 익지 못한 상황

        return redTomato> 0 && greenTomato == 0 ? 1 : 0;
    }

    static boolean oob(int r, int c) {

        return r < 0 || c < 0 || r >= N || c >= M;
    }

    static void bfs(Queue<int[]> q) {
        int size = q.size();
        for (int l = 0; l < size ; l++) {
            //붉은 토마토 돌면서 상하좌우 번지게
            int[] cur = q.poll();
            int i = cur[0];
            int j = cur[1];

            //붉은 토마토면 번지게
            for (int d = 0; d < 4; d++) {
                int nr = i + dr[d];
                int nc = j + dc[d];

                if (oob(nr, nc) || visited[nr][nc])
                    continue;
                if (tomatoBox[nr][nc] == -1)
                    continue;

                //붉은 토마토면 상하좌우 탐색후 번질 토마토 주워담기
                if (tomatoBox[nr][nc] == 0) {
                    tomatoBox[nr][nc] = 1;
                    q.offer(new int[] { nr, nc });
                }

            }

            //현재의 토마토는 붉게하기
            visited[i][j] = true;

        }
    }

}