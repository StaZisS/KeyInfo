import {
    Button,
    Col,
    Container,
    Modal, ModalBody,
    ModalHeader,
    Row
} from "react-bootstrap";
import DateHelper from "../../helpers/DateHelper";
import {useMutation, useQueryClient} from "react-query";
import KeyService from "../../services/KeyService";

export const AcceptedApplicationsModal = (props) => {

    const queryClient = useQueryClient()
    const mutation = useMutation((keyHolderId) => KeyService.giveKey(props.key_id,keyHolderId),{
        onSuccess(){
            queryClient.invalidateQueries(['keys'])
        }
    })

    const handleGet = (keyHolderId) => {
        mutation.mutate(keyHolderId)
        props.onHide()
    }

    return (
        <Modal {...props} size={'lg'} centered autoFocus={true}>
            <ModalHeader className='d-flex'>
                <div className='d-flex gap-3'>
                    <Modal.Title>Здание {props.build}</Modal.Title>
                    <Modal.Title>Аудитория {props.room}</Modal.Title>
                </div>
                <Modal.Title>Одобренные заявки</Modal.Title>
            </ModalHeader>

            <ModalBody>
                <Container>
                    <Row className={'d-flex text-start border-bottom mb-1'}>
                        <Col className='fw-bold'>Дата</Col>
                        <Col className='fw-bold'>Время</Col>
                        <Col className='fw-bold'>Email</Col>
                        <Col className='fw-bold'>ФИО</Col>
                        <Col className='fw-bold'></Col>
                    </Row>
                </Container>
                <Container>
                    {props.data.map((app) => {
                        const startTime = DateHelper.normalizeDate(app.start_time)
                        const endTime = DateHelper.normalizeDate(app.end_time)
                        return (
                            <Row key={app.application_id}
                                 className='text-start border-bottom mb-3 d-flex align-items-center'>
                                <Col>
                                    {startTime.day}.{startTime.month}.{startTime.year}
                                </Col>
                                <Col>
                                    {startTime.hours}:{startTime.minutes} - {endTime.hours}:{endTime.minutes}
                                </Col>

                                <Col>
                                    {app.owner_name}
                                </Col>
                                <Col>
                                    {app.owner_email}
                                </Col>
                                <Col>
                                    <Button onClick={() => handleGet(app.owner_id)} className={'btn rounded-5 btn-success mb-1'}>
                                        Отдать
                                    </Button>
                                </Col>
                            </Row>

                        )
                    })}

                </Container>
            </ModalBody>
        </Modal>
    )
}