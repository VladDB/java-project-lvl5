package hexlet.code.service;

import hexlet.code.dto.LabelDto;
import hexlet.code.exeptions.UserNotFoundException;
import hexlet.code.model.Label;
import hexlet.code.repository.LabelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LabelServiceImpl implements LabelService {

    @Autowired
    private LabelRepository labelRepository;

    @Override
    public Label createNewLabel(LabelDto labelDto) {
        Label label = new Label();

        label.setName(labelDto.getName());

        return labelRepository.save(label);
    }

    @Override
    public Label updateLabel(long id, LabelDto labelDto) {
        Label label = labelRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Label does not exist"));

        label.setName(labelDto.getName());

        return labelRepository.save(label);
    }
}
