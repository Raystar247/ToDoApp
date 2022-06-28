package jp.kobespiral.rens.todo.service;

import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

import jp.kobespiral.rens.todo.dto.ToDoForm;
import jp.kobespiral.rens.todo.entity.ToDo;
import jp.kobespiral.rens.todo.exception.ToDoAppException;
import jp.kobespiral.rens.todo.repository.ToDoRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ToDoService {
    private final ToDoRepository tdRepo;

    /**
     * メンバーを作成する(C)
     * 
     * @param form
     * @return
     */
    public ToDo createToDo(String mid, ToDoForm form) {
        Date dateObj = new Date();
        String title = form.title;
        ToDo todo = new ToDo(null, title, mid, false, dateObj, null);
        return tdRepo.save(todo);

    }

    public ToDo getToDo(Long seq) {
        /* 番号を指定してToDoを取得 */
        ToDo todo = tdRepo.findById(seq).orElseThrow(
                () -> new ToDoAppException(ToDoAppException.NO_SUCH_MEMBER_EXISTS, seq + ": No such member exists"));
        return todo;
    }

    public List<ToDo> getToDoList(String mid) {
        /* midのToDoリストを取得 */
        List<ToDo> l = tdRepo.findByMidAndDone(mid, false);
        return l;
    }

    public List<ToDo> getDoneList(String mid) {
        /* midのDoneリストを取得 */
        List<ToDo> l = tdRepo.findByMidAndDone(mid, true);
        return l;
    }

    public List<ToDo> getToDoList() {
        /* 全員のToDoリストを取得 */
        List<ToDo> l = tdRepo.findByDone(false);
        return l;
    }

    public List<ToDo> getDoneList() {
        /* 全員のDoneリストを取得 */
        List<ToDo> l = tdRepo.findByDone(true);
        return l;
    }

    public ToDo revertTask(Long seq) {
        ToDo todo = tdRepo.findById(seq).orElseThrow(
                () -> new ToDoAppException(ToDoAppException.NO_SUCH_MEMBER_EXISTS, seq + ": No such member exists"));

        Date dateObj = new Date();
        todo.doneAt = dateObj;
        todo.done = !todo.done;
        return tdRepo.save(todo);
    }
}