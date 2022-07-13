package spring.project.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import lombok.Setter;
import spring.project.mapper.Post_BoardAttachMapper;
import spring.project.mapper.Post_BoardMapper;
import spring.project.model.Post_BoardAttachDTO;
import spring.project.model.Post_BoardDTO;

@Service
public class Post_BoardServiceImpl implements Post_BoardService {

	@Setter(onMethod_= @Autowired)
	private Post_BoardMapper pbMapper;
	
	@Setter(onMethod_= @Autowired)
	private Post_BoardAttachMapper pbAMapper;
	
	//httprequest servle>>> �Ű����� getrealpath
	@Transactional
	@Override
	public int addPost_Board(Post_BoardDTO board, 
			@RequestParam("file") MultipartFile[] files) {
		System.out.println(board);
		System.out.println(files);
		List<Post_BoardAttachDTO> list = new ArrayList<>();
		String uploadFolder = "C:\\upload"; //����
	//	String uploadFolderPath = getFolder(); ��δ� ���߿� �ð� ������ ��¥���� �����
		
		for(MultipartFile f : files) {
			if(!f.isEmpty()) {
				Post_BoardAttachDTO attachDTO = new Post_BoardAttachDTO();
				String uploadFileName = f.getOriginalFilename();
				attachDTO.setFileName(uploadFileName);	// attachDTO FileName�� ���� ���ϸ� ����
				
				UUID uuid = UUID.randomUUID();	// ������ȣ�� ���� ����
				uploadFileName = uuid.toString() + "_" + uploadFileName;	// ���Ͽ��� �����Ҷ� �ߺ������� UUID�� ���ϸ��� ���� ���ο� ���ϸ����� ����
				
				try {
					File saveFile = new File(uploadFolder, uploadFileName);
					f.transferTo(saveFile);
					
					attachDTO.setUuid(uuid.toString());
					attachDTO.setUploadPath(uploadFolder);
					
					list.add(attachDTO);	// �޾ƿ� ���ϵ��� list�� ����
					
					System.out.println("attachDTO��" + attachDTO);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		if(!list.isEmpty()) {
			System.out.println("list�� board�� �ֱ� ���� Ȯ��");
			board.setAttachList(list);	// Post_BoardDTO�� attachList(�迭)�� list ����
		}
		
		System.out.println("���� Board��: " + board);
			
		// post_group ������ +1 �Ͽ� ���ο� �׷츸���, ������ ���� �޾Ƽ� ������ �� addPost_Board ����
		int post_group = pbMapper.maxPost_group()+1;
		if(board.getPost_group() != 0) {
			board.setPost_group(board.getPost_group());
			board.setPost_level(1);
		}else {
			board.setPost_group(post_group);
		}
		int result = pbMapper.addPost_Board(board);
		System.out.println("pnum ��?:"+result);
		// Post_BoardDTO�� attachList���� ������ �״�� ����
		if(board.getAttachList() == null || board.getAttachList().size() <= 0) {
			System.out.println("����ü�� �۵��� �ϴ°ǰ���!");
			return result;
		}
		
		// attachList�� ���� Post_BoardAttach DB�� �־���
		board.getAttachList().forEach(attach ->{
			attach.setPnum(board.getPnum());
			System.out.println("attach�� ����ִ��� Ȯ���ϱ�" + attach);
			pbAMapper.addPost_BoardAttach(attach);
		});
		
		return result;	// ���� �����ϸ� post_board ���ุ ī��Ʈ�ϹǷ� 1
	}
	
	@Override
	public List<Post_BoardDTO> post_BoardLists(int startRow, int endRow, String board_type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int post_BoardCount(String board_type) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Post_BoardDTO post_BoardContent(int pnum) {
		List<Post_BoardAttachDTO> fileList = pbAMapper.getPost_BoardAtachList(pnum);
		Post_BoardDTO dto = pbMapper.post_BoardContent(pnum);
//		dto.setFileList(fileList);
		return dto;
	}
	

	@Override
	public int modPost_Board(Post_BoardDTO dto) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int delPost_Board(int pnum) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int delPost_BoardAttach(int pnum) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int passwdCheck(String memid, String passwd) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int upReadCnt(Post_BoardDTO dto) {
		// TODO Auto-generated method stub
		return 0;
	}


	
}